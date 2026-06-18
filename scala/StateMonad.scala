final class State[S, A](val run: S => (S, A)):
  def map[B](f: A => B): State[S, B] =
    State(s => { val (s2, a) = run(s); (s2, f(a)) })
  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State(s => { val (s2, a) = run(s); f(a).run(s2) })

object State:
  def pure[S, A](a: A): State[S, A]        = State(s => (s, a))
  def get[S]: State[S, S]                  = State(s => (s, s))
  def set[S](s: S): State[S, Unit]         = State(_ => (s, ()))
  def modify[S](f: S => S): State[S, Unit] = State(s => (f(s), ()))

@main def run(): Unit =
  val counter: State[Int, String] =
    for
      n <- State.get[Int]
      _ <- State.modify[Int](_ + 1)
      m <- State.get[Int]
    yield s"was $n, now $m"
  println(counter.run(0))

// State monad threads state automatically; plain version must pass and return it by hand
def counterPlain(s: Int): (Int, String) = (s + 1, s"was $s, now ${s + 1}")

def push(x: Int): State[List[Int], Unit] = State.modify(x :: _)
def pop: State[List[Int], Int]           = State(s => (s.tail, s.head))

def sequence[S, A](ss: List[State[S, A]]): State[S, List[A]] =
  ss.foldRight(State.pure[S, List[A]](Nil)) { (s, acc) =>
    for a <- s; as <- acc yield a :: as
  }

@main def run2(): Unit =
  val stackOps =
    for
      _ <- push(1)
      _ <- push(2)
      _ <- push(3)
      a <- pop
      b <- pop
    yield (a, b)
  println(stackOps.run(Nil))
  println(sequence(List.fill(3)(State.modify[Int](_ + 1))).run(0))
