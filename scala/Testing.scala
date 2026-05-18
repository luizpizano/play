//> using dep org.scalatest::scalatest::3.2.18

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

// ============================================================
// SUBJECT CODE (functions under test)
// ============================================================

def add(a: Int, b: Int): Int = a + b

def divide(a: Int, b: Int): Option[Int] =
  if b == 0 then None else Some(a / b)

def validateAge(age: Int): Either[String, Int] =
  if age < 0 then Left("negative")
  else if age > 150 then Left("too large")
  else Right(age)

case class Point(x: Int, y: Int):
  def distanceTo(other: Point): Double =
    math.sqrt(math.pow(x - other.x, 2) + math.pow(y - other.y, 2))

// ============================================================
// FunSuite — simplest style, one test per method call
// ============================================================

class MathSuite extends AnyFunSuite with Matchers:

  test("add: positive numbers") {
    add(2, 3) shouldBe 5
  }

  test("add: negative numbers") {
    add(-1, -4) shouldBe -5
  }

  test("add: identity with zero") {
    add(7, 0) shouldBe 7
  }

  test("divide: normal case returns Some") {
    divide(10, 2) shouldBe Some(5)
  }

  test("divide: by zero returns None") {
    divide(10, 0) shouldBe None
  }

// ============================================================
// FlatSpec — BDD style: "subject" should "behavior"
// ============================================================

class ValidationSpec extends AnyFlatSpec with Matchers:

  "validateAge" should "accept a valid age" in {
    validateAge(25) shouldBe Right(25)
  }

  it should "reject a negative age" in {
    validateAge(-1) shouldBe Left("negative")
  }

  it should "reject an unrealistically large age" in {
    validateAge(200) shouldBe Left("too large")
  }

  it should "accept boundary value 0" in {
    validateAge(0) shouldBe Right(0)
  }

  it should "accept boundary value 150" in {
    validateAge(150) shouldBe Right(150)
  }

// ============================================================
// WordSpec — nested describe/should/in blocks
// ============================================================

class PointSpec extends AnyWordSpec with Matchers:

  "Point" when {
    "created" should {
      "hold x and y fields" in {
        val p = Point(3, 4)
        p.x shouldBe 3
        p.y shouldBe 4
      }
    }

    "measuring distance" should {
      "return 0.0 to itself" in {
        val p = Point(1, 1)
        p.distanceTo(p) shouldBe 0.0
      }

      "calculate 3-4-5 right triangle hypotenuse" in {
        Point(0, 0).distanceTo(Point(3, 4)) shouldBe 5.0
      }
    }
  }

// ============================================================
// MATCHERS showcase — various built-in matchers
// ============================================================

class MatchersSuite extends AnyFunSuite with Matchers:

  // equality
  test("shouldBe and shouldEqual") {
    1 + 1 shouldBe 2
    "hello" shouldEqual "hello"
  }

  // comparison
  test("comparison matchers") {
    10 should be > 5
    3  should be < 7
    5  should be >= 5
    5  should be <= 5
  }

  // string matchers
  test("string matchers") {
    "scala" should startWith ("sca")
    "scala" should endWith   ("ala")
    "scala" should include   ("cal")
    "scala" should have length 5
  }

  // collection matchers
  test("collection matchers") {
    List(1, 2, 3) should contain (2)
    List(1, 2, 3) should have length 3
    List(1, 2, 3) should not be empty
    List.empty[Int] shouldBe empty
    List(1, 2, 3) should contain allOf (1, 3)
    List(1, 2, 3) should contain noElementsOf List(4, 5)
  }

  // Option matchers
  test("Option matchers") {
    Some(42) shouldBe defined
    None     should not be defined
    Some(42).value shouldBe 42
  }

  // Either matchers
  test("Either matchers") {
    val r: Either[String, Int] = Right(10)
    val l: Either[String, Int] = Left("err")
    r shouldBe a [Right[?, ?]]
    l shouldBe a [Left[?, ?]]
    r.getOrElse(0) shouldBe 10
  }

  // exception matchers
  test("exception matchers") {
    an [ArithmeticException] should be thrownBy { val _ = 1 / 0 }
    the [ArithmeticException] thrownBy { val _ = 5 / 0 } should have message "/ by zero"
  }
