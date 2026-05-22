// --- PASSING FUNCTIONS AS ARGUMENTS ---
def applyTwice(f: Int => Int, x: Int): Int = f(f(x))

def transform(xs: List[Int], f: Int => Int): List[Int] = xs.map(f)

def keep(xs: List[Int], predicate: Int => Boolean): List[Int] = xs.filter(predicate)

// --- RETURNING FUNCTIONS ---
def multiplier(factor: Int): Int => Int = x => x * factor

def adder(n: Int): Int => Int = _ + n

def greaterThan(threshold: Int): Int => Boolean = _ > threshold

// --- FUNCTION COMPOSITION ---
def compose[A, B, C](f: B => C, g: A => B): A => C = x => f(g(x))

val doubleStr: Int => String = compose(_.toString, _ * 2)

// --- CLOSURES ---
def counter(): () => Int =
  var count = 0
  () => { count += 1; count }

def makeAccumulator(initial: Int): Int => Int =
  var total = initial
  n => { total += n; total }

// --- FOLD & REDUCE ---
def mySum(xs: List[Int]): Int     = xs.foldLeft(0)(_ + _)
def myProduct(xs: List[Int]): Int = xs.foldLeft(1)(_ * _)
def myMax(xs: List[Int]): Int     = xs.reduce((a, b) => if a > b then a else b)

// --- PIPELINE (ANDTHEN / COMPOSE) ---
val normalize: String => String = _.trim.toLowerCase
val exclaim: String => String   = s => s"$s!"
val shout: String => String     = normalize andThen exclaim andThen (_.toUpperCase)

@main def run(): Unit =
  // passing functions as arguments
  println(applyTwice(_ + 3, 10))
  println(applyTwice(_ * 2, 5))
  println(transform(List(1, 2, 3), _ * 10))
  println(keep(List(1, 2, 3, 4, 5), _ % 2 == 0))

  // returning functions
  val triple = multiplier(3)
  println(triple(7))
  println(multiplier(5)(4))
  val add5 = adder(5)
  println(add5(10))
  val isAdult = greaterThan(17)
  println(isAdult(18))
  println(isAdult(16))

  // function composition
  println(doubleStr(21))
  val doubleAndNegate = compose((x: Int) => -x, (x: Int) => x * 2)
  println(doubleAndNegate(5))

  // closures
  val c = counter()
  println(c())
  println(c())
  println(c())
  val acc = makeAccumulator(100)
  println(acc(10))
  println(acc(20))

  // fold & reduce
  println(mySum(List(1, 2, 3, 4, 5)))
  println(myProduct(List(1, 2, 3, 4, 5)))
  println(myMax(List(3, 1, 4, 1, 5, 9, 2)))
  println(List(1, 2, 3).foldLeft("")((acc, x) => acc + x.toString))
  println(List(1, 2, 3).foldRight("")((x, acc) => acc + x.toString))

  // pipeline
  println(shout("  Hello World  "))
  println(List("  foo  ", "BAR", " baz").map(normalize))
