// --- FUNCTIONS ---
def add(a: Int, b: Int): Int = a + b

def greet(name: String = "World"): String = s"Hello, $name!"

def factorial(n: Int): Int =
  if n <= 1 then 1 else n * factorial(n - 1)

// --- CLASSES ---
class Animal(val name: String, val sound: String):
  def speak(): String = s"$name says $sound"

class Dog(name: String) extends Animal(name, "Woof"):
  override def speak(): String = super.speak() + "!"

// --- TRAITS ---
trait Flyable:
  def fly(): String

trait Swimmable:
  def swim(): String = s"${toString} is swimming"  // default implementation

class Duck(val name: String) extends Flyable with Swimmable:
  def fly(): String             = s"$name is flying"
  override def toString: String = name

// --- OBJECTS (singleton) ---
object Counter:
  private var value = 0
  def increment(): Unit = value += 1
  def get: Int          = value

// --- CASE CLASSES ---
case class Point(x: Double, y: Double):
  def distanceTo(other: Point): Double =
    math.sqrt(math.pow(other.x - x, 2) + math.pow(other.y - y, 2))

// --- CASE OBJECTS ---
case object Origin:
  val x = 0.0
  val y = 0.0

// --- SEALED TRAITS (ADT / discriminated union) ---
sealed trait Shape
case class Circle(radius: Double)                   extends Shape
case class Rectangle(width: Double, height: Double) extends Shape
case object Unknown                                 extends Shape

def area(s: Shape): Double = s match
  case Circle(r)       => math.Pi * r * r
  case Rectangle(w, h) => w * h
  case Unknown         => 0.0

sealed trait Direction
case object North extends Direction
case object South extends Direction
case object East  extends Direction
case object West  extends Direction

def opposite(d: Direction): Direction = d match
  case North => South
  case South => North
  case East  => West
  case West  => East

@main def run(): Unit =
  // VARIABLES
  val pi    = 3.14159   // val: immutable (like final in Java)
  var count = 0         // var: mutable
  count += 1
  println(pi)
  println(count)

  // FUNCTIONS
  println(add(3, 4))
  println(greet())
  println(greet("Scala"))
  println(factorial(5))

  // CLASSES
  val dog = Dog("Rex")
  println(dog.speak())
  println(dog.name)

  // TRAITS
  val duck = Duck("Donald")
  println(duck.fly())
  println(duck.swim())

  // OBJECTS (singleton)
  Counter.increment()
  Counter.increment()
  println(Counter.get)

  // CASE CLASSES
  val p1 = Point(0, 0)
  val p2 = Point(3, 4)
  println(p1.distanceTo(p2))
  val p3 = p2.copy(y = 0)
  println(p3)
  println(p1 == Point(0, 0))

  // CASE OBJECTS
  println(Origin)
  println(Origin.x)

  // SEALED TRAITS — pattern matching is exhaustive (compiler warns if a case is missing)
  println(area(Circle(5)))
  println(area(Rectangle(3, 4)))
  println(area(Unknown))
  println(opposite(North))
  println(opposite(East))
