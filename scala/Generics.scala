class Box[T](val value: T):
  def map[U](f: T => U): Box[U] = Box(f(value))
  override def toString: String  = s"Box($value)"

class Pair[A, B](val first: A, val second: B):
  def swap: Pair[B, A]          = Pair(second, first)
  override def toString: String = s"($first, $second)"

trait Named:
  def name: String

class NamedBox[T <: Named](val item: T):
  def label: String = item.name

case class Cat(name: String) extends Named

class ReadOnlyList[+T](private val items: List[T]):
  def head: T                   = items.head
  def size: Int                 = items.size
  override def toString: String = s"ReadOnlyList($items)"

class Logger[-T]:
  def log(value: T): Unit = println(s"[LOG] $value")

sealed trait Maybe[+A]:
  def map[B](f: A => B): Maybe[B]
  def getOrElse[B >: A](default: B): B

case class Just[A](value: A) extends Maybe[A]:
  def map[B](f: A => B): Maybe[B]      = Just(f(value))
  def getOrElse[B >: A](default: B): B = value

case object Nothing extends Maybe[Nothing]:
  def map[B](f: Nothing => B): Maybe[B]          = Nothing
  def getOrElse[B >: Nothing](default: B): B     = default

def identity[A](x: A): A = x

def max[T: Ordering](a: T, b: T): T =
  if summon[Ordering[T]].gt(a, b) then a else b

trait Stringify[T]:
  def stringify(value: T): String

given Stringify[Int] with
  def stringify(value: Int): String = s"Int($value)"

given Stringify[String] with
  def stringify(value: String): String = s"\"$value\""

given [T: Stringify]: Stringify[List[T]] with
  def stringify(value: List[T]): String =
    value.map(summon[Stringify[T]].stringify).mkString("[", ", ", "]")

def show[T: Stringify](value: T): String = summon[Stringify[T]].stringify(value)

@main def run(): Unit =
  val box = Box(42)
  println(box)
  println(box.map(_ * 2))
  println(box.map(_.toString))

  val pair = Pair("hello", 42)
  println(pair)
  println(pair.swap)

  val namedBox = NamedBox(Cat("Whiskers"))
  println(namedBox.label)

  val rol = ReadOnlyList(List(1, 2, 3))
  println(rol)
  println(rol.head)

  val anyLogger: Logger[Any]    = Logger[Any]()
  val strLogger: Logger[String] = anyLogger
  strLogger.log("hello contravariance")

  val just: Maybe[Int]    = Just(10)
  val nothing: Maybe[Int] = Nothing
  println(just.map(_ * 3))
  println(nothing.map(_ * 3))
  println(just.getOrElse(0))
  println(nothing.getOrElse(0))

  println(identity(42))
  println(identity("scala"))

  println(max(3, 7))
  println(max("apple", "banana"))

  println(show(42))
  println(show("hello"))
  println(show(List(1, 2, 3)))
