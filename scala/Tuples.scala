// --- TUPLE CREATION ---
val pair    = (1, "hello")
val triple  = (3.14, true, 'z')
val single  = Tuple1(42)

// --- NAMED ACCESS ---
val point   = (10, 20)
val x       = point._1
val y       = point._2

// --- DESTRUCTURING ---
val (a, b)  = pair
val (pi, flag, ch) = triple

// --- SWAP ---
val swapped = pair.swap

// --- TUPLE AS RETURN VALUE ---
def minMax(xs: List[Int]): (Int, Int) = (xs.min, xs.max)

// --- ZIPPING PRODUCES TUPLES ---
val zipped  = List(1, 2, 3).zip(List("a", "b", "c"))

// --- PATTERN MATCHING ON TUPLES ---
def describe(t: (Int, Int)): String = t match
  case (0, 0) => "origin"
  case (x, 0) => s"on x-axis at $x"
  case (0, y) => s"on y-axis at $y"
  case (x, y) => s"point ($x, $y)"

// --- TUPLE TO LIST ---
val t3      = (1, 2, 3)
val asList  = t3.toList

// --- NESTED TUPLES ---
val nested  = ((1, 2), (3, 4))
val inner   = nested._1

@main def run(): Unit =
  // CREATION
  println(pair)
  println(triple)
  println(single)

  // NAMED ACCESS
  println(x)
  println(y)

  // DESTRUCTURING
  println(a)
  println(b)
  println(pi)
  println(flag)
  println(ch)

  // SWAP
  println(swapped)

  // TUPLE AS RETURN VALUE
  val (lo, hi) = minMax(List(5, 1, 8, 2, 9, 3))
  println(lo)
  println(hi)

  // ZIPPING
  println(zipped)
  zipped.foreach { case (n, s) => println(s"$n -> $s") }

  // PATTERN MATCHING
  println(describe((0, 0)))
  println(describe((5, 0)))
  println(describe((0, 3)))
  println(describe((4, 7)))

  // TUPLE TO LIST
  println(asList)

  // NESTED TUPLES
  println(nested)
  println(inner)
  println(inner._1)
