trait Show[A]:
  def show(a: A): String

given Show[Int] with
  def show(a: Int): String = s"Int($a)"

given Show[String] with
  def show(a: String): String = s"String(\"$a\")"

given Show[Boolean] with
  def show(a: Boolean): String = if a then "yes" else "no"

def display[A](a: A)(using s: Show[A]): String = s.show(a)

case class Celsius(value: Double):
  def toFahrenheit: Double = value * 9.0 / 5.0 + 32

given Conversion[Double, Celsius] = Celsius(_)


case class Config(prefix: String)

def log(msg: String)(using cfg: Config): String =
  s"[${cfg.prefix}] $msg"

def process(data: String)(using Config): String =
  log(s"processing '$data'")

@main def run(): Unit =
  println("Type class (Show):")
  println(s"  ${display(42)}")
  println(s"  ${display("hello")}")
  println(s"  ${display(true)}")

  println("\nImplicit conversion (Double -> Celsius):")
  val temp: Celsius = 100.0
  println(s"  100.0°C = ${temp.toFahrenheit}°F")

  println("\nContext propagation (Config):")
  given Config = Config("APP")
  println(s"  ${process("user-data")}")

  println("\nSummoning a given:")
  val showInt = summon[Show[Int]]
  println(s"  ${showInt.show(99)}")
