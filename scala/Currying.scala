// --- CURRYING ---
def add(a: Int)(b: Int): Int = a + b

val add10: Int => Int = add(10)

def curriedMultiply(a: Int)(b: Int): Int = a * b

@main def run(): Unit =
  println(add(3)(4))
  println(add10(7))
  val times3 = curriedMultiply(3)
  println(times3(9))
  println(List(1, 2, 3, 4).map(add10))
