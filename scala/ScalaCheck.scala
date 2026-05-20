//> using dep org.scalacheck::scalacheck::1.19.0

import org.scalacheck.{Gen, Arbitrary, Prop, Properties}
import org.scalacheck.Prop.{forAll, propBoolean}

// ============================================================
// SUBJECT CODE
// ============================================================

def reverseList[A](xs: List[A]): List[A] = xs.reverse

def safeDiv(a: Int, b: Int): Option[Int] =
  if b == 0 then None else Some(a / b)

def clamp(value: Int, min: Int, max: Int): Int =
  math.max(min, math.min(max, value))

case class Temperature(celsius: Double):
  def toFahrenheit: Double = celsius * 9.0 / 5.0 + 32
  def toKelvin: Double     = celsius + 273.15

// ============================================================
// BASIC — forAll with built-in Arbitrary
// ============================================================

object BasicProps extends Properties("Basic"):

  property("reverse twice is identity") = forAll { (xs: List[Int]) =>
    reverseList(reverseList(xs)) == xs
  }

  property("reverse preserves length") = forAll { (xs: List[Int]) =>
    reverseList(xs).length == xs.length
  }

// ============================================================
// CONDITIONAL — filter inputs with ==>
// ============================================================

object ConditionalProps extends Properties("Conditional"):

  property("safeDiv: non-zero denominator") = forAll { (a: Int, b: Int) =>
    b != 0 ==> (safeDiv(a, b) == Some(a / b))
  }

  property("clamp: result always within [min, max]") = forAll {
    (value: Int, min: Int, max: Int) =>
      min <= max ==> {
        val result = clamp(value, min, max)
        result >= min && result <= max
      }
  }

// ============================================================
// CUSTOM GENERATORS — Gen combinators
// ============================================================

object GeneratorProps extends Properties("Generators"):

  val genEmail: Gen[String] = for
    user   <- Gen.alphaStr.suchThat(_.nonEmpty)
    domain <- Gen.alphaStr.suchThat(_.nonEmpty)
    tld    <- Gen.oneOf("com", "org", "net")
  yield s"$user@$domain.$tld"

  property("generated emails always contain @ and .") = forAll(genEmail) { email =>
    email.contains("@") && email.contains(".")
  }

// ============================================================
// ARBITRARY — implicit generator for a custom type
// ============================================================

object ArbitraryProps extends Properties("Arbitrary"):

  given Arbitrary[Temperature] = Arbitrary(
    Gen.choose(-273.15, 1_000_000.0).map(Temperature(_))
  )

  property("Kelvin is always >= 0") = forAll { (t: Temperature) =>
    t.toKelvin >= 0.0
  }

  property("celsius -> fahrenheit -> celsius round-trip") = forAll { (t: Temperature) =>
    val back = (t.toFahrenheit - 32) * 5.0 / 9.0
    math.abs(back - t.celsius) < 1e-9
  }

// ============================================================
// COLLECT — observe input distribution
// ============================================================

object CollectProps extends Properties("Collect"):

  property("list length distribution") = forAll { (xs: List[Int]) =>
    Prop.classify(xs.isEmpty,     "empty") {
    Prop.classify(xs.length > 10, "large") {
      reverseList(reverseList(xs)) == xs
    }}
  }

// ============================================================
// RUN ALL
// ============================================================

@main def runScalaCheck(): Unit =
  List(BasicProps, ConditionalProps, GeneratorProps, ArbitraryProps, CollectProps)
    .foreach(_.check())
