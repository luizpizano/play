// --- SEALED TRAIT ADT ---
sealed trait Expr
case class Num(value: Double)            extends Expr
case class Add(left: Expr, right: Expr)  extends Expr
case class Mul(left: Expr, right: Expr)  extends Expr
case object Zero                         extends Expr

def eval(e: Expr): Double = e match
  case Zero         => 0.0
  case Num(v)       => v
  case Add(l, r)    => eval(l) + eval(r)
  case Mul(l, r)    => eval(l) * eval(r)

// --- OPTION ---
def divide(a: Int, b: Int): Option[Int] =
  if b == 0 then None else Some(a / b)

def describeOption(o: Option[Int]): String = o match
  case Some(v) => s"got $v"
  case None    => "nothing"

// --- EITHER ---
def safeSqrt(n: Double): Either[String, Double] =
  if n < 0 then Left(s"$n is negative") else Right(math.sqrt(n))

// --- TUPLE MATCHING ---
def quadrant(x: Int, y: Int): String = (x, y) match
  case (0, 0)                        => "origin"
  case (px, 0) if px > 0             => "positive x-axis"
  case (0, py) if py > 0             => "positive y-axis"
  case (px, py) if px > 0 && py > 0  => "Q1"
  case (px, py) if px < 0 && py > 0  => "Q2"
  case (px, py) if px < 0 && py < 0  => "Q3"
  case _                             => "Q4"

// --- LIST MATCHING ---
def describe(list: List[Int]): String = list match
  case Nil              => "empty"
  case x :: Nil         => s"one element: $x"
  case x :: y :: Nil    => s"two elements: $x and $y"
  case head :: tail     => s"starts with $head, ${tail.length} more"

// --- TYPE MATCHING ---
def whatIs(x: Any): String = x match
  case i: Int    => s"Int: $i"
  case s: String => s"String of length ${s.length}"
  case d: Double => s"Double: $d"
  case _         => "something else"

// --- GUARDS ---
def classify(n: Int): String = n match
  case 0                => "zero"
  case n if n < 0       => "negative"
  case n if n % 2 == 0  => "positive even"
  case _                => "positive odd"

@main def runPatternMatching(): Unit =
  // SEALED TRAIT (expression tree)
  val expr = Add(Mul(Num(3), Num(4)), Num(2))
  println(eval(expr))
  println(eval(Zero))

  // OPTION
  println(describeOption(divide(10, 2)))
  println(describeOption(divide(10, 0)))

  // EITHER
  safeSqrt(9.0)  match { case Right(v) => println(v); case Left(e) => println(e) }
  safeSqrt(-1.0) match { case Right(v) => println(v); case Left(e) => println(e) }

  // TUPLE
  println(quadrant(3, 5))
  println(quadrant(-1, 2))
  println(quadrant(0, 0))

  // LIST
  println(describe(List()))
  println(describe(List(42)))
  println(describe(List(1, 2, 3, 4)))

  // TYPE
  println(whatIs(7))
  println(whatIs("hello"))
  println(whatIs(3.14))

  // GUARDS
  println(classify(0))
  println(classify(-3))
  println(classify(4))
  println(classify(7))
