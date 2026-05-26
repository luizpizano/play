// --- TAIL RECURSION ---
import scala.annotation.tailrec

// Regular recursion — stack frame per call
def factorial(n: Int): BigInt =
  if n <= 1 then 1 else n * factorial(n - 1)

// Tail-recursive — accumulator carries the result, no pending work after the call
def factorialTR(n: Int, acc: BigInt = 1): BigInt =
  if n <= 1 then acc else factorialTR(n - 1, n * acc)

// @tailrec makes the compiler verify the recursion is in tail position
@tailrec
def sum(n: Int, acc: Int = 0): Int =
  if n <= 0 then acc else sum(n - 1, acc + n)

@main def run(): Unit =
  println(factorial(10))
  println(factorialTR(10))
  println(sum(100))
  println(factorialTR(10000).toString.take(20) + "...")
