trait Monoid[A]:
  def empty: A
  def combine(x: A, y: A): A

final class Writer[W, A](val run: (W, A))(using W: Monoid[W]):
  def map[B](f: A => B): Writer[W, B] = Writer((run._1, f(run._2)))
  def flatMap[B](f: A => Writer[W, B]): Writer[W, B] =
    val (w2, b) = f(run._2).run
    Writer((W.combine(run._1, w2), b))

given Monoid[String] with
  def empty = ""
  def combine(x: String, y: String) = x + y

def addPlain(x: Int, y: Int): (String, Int) =
  val sum = x + y
  (s"added $x + $y = $sum\n", sum)
