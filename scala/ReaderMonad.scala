final class Reader[R, A](val run: R => A):
  def map[B](f: A => B): Reader[R, B]               = Reader(r => f(run(r)))
  def flatMap[B](f: A => Reader[R, B]): Reader[R, B] = Reader(r => f(run(r)).run(r))

object Reader:
  def pure[R, A](a: A): Reader[R, A]                         = Reader(_ => a)
  def ask[R]: Reader[R, R]                                    = Reader(identity)
  def asks[R, A](f: R => A): Reader[R, A]                    = Reader(f)
  def local[R, A](f: R => R)(ra: Reader[R, A]): Reader[R, A] = Reader(r => ra.run(f(r)))
