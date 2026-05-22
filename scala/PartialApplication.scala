// --- PARTIAL APPLICATION ---
def power(base: Int, exp: Int): Int = math.pow(base, exp).toInt

val square: Int => Int = power(_, 2)
val cube: Int => Int   = power(_, 3)

@main def run(): Unit =
  println(square(6))
  println(cube(3))
  println(List(1, 2, 3, 4).map(square))
