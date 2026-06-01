// --- IMPLICITS (Scala 3 style: given/using) ---

// 1. Type class pattern: define a type class and instances
trait Show[A]:
  def show(a: A): String

given Show[Int] with
  def show(a: Int): String = s"Int($a)"

given Show[String] with
  def show(a: String): String = s"String(\"$a\")"

given Show[Boolean] with
  def show(a: Boolean): String = if a then "yes" else "no"

def display[A](a: A)(using s: Show[A]): String = s.show(a)

// 2. Implicit conversion: wrap an Int as a "rich" type
case class Celsius(value: Double):
  def toFahrenheit: Double = value * 9.0 / 5.0 + 32

given Conversion[Double, Celsius] = Celsius(_)


@main def run(): Unit =
  // Type class dispatch
  println("Type class (Show):")
  println(s"  ${display(42)}")
  println(s"  ${display("hello")}")
  println(s"  ${display(true)}")

  // Implicit conversion
  println("\nImplicit conversion (Double -> Celsius):")
  val temp: Celsius = 100.0
  println(s"  100.0°C = ${temp.toFahrenheit}°F")

  // Summoning a given directly with summon
  println("\nSummoning a given:")
  val showInt = summon[Show[Int]]
  println(s"  ${showInt.show(99)}")
