final class IO[A](val unsafeRun: () => A):
  def map[B](f: A => B): IO[B]        = IO(() => f(unsafeRun()))
  def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(unsafeRun()).unsafeRun())
  def attempt: IO[Either[Throwable, A]] = IO(() =>
    try Right(unsafeRun()) catch case e: Throwable => Left(e)
  )
  def handleError(f: Throwable => A): IO[A] = IO(() =>
    try unsafeRun() catch case e: Throwable => f(e)
  )

object IO:
  def pure[A](a: A): IO[A]              = IO(() => a)
  def delay[A](a: => A): IO[A]          = IO(() => a)
  def raiseError[A](e: Throwable): IO[A] = IO(() => throw e)
  def sequence[A](ios: List[IO[A]]): IO[List[A]] =
    ios.foldRight(IO.pure(List.empty[A])) { (io, acc) =>
      for a <- io; as <- acc yield a :: as
    }

// IO monad separates describing effects from running them — enables composition, error handling, and referential transparency
def greetPlain(name: String): Unit =
  println(s"Hello, $name!")
  println(s"${name.length} chars in name")

def greet(name: String): IO[Unit] =
  for
    _ <- IO.delay(println(s"Hello, $name!"))
    _ <- IO.delay(println(s"${name.length} chars in name"))
  yield ()

def safeDivide(a: Int, b: Int): IO[Int] =
  if b == 0 then IO.raiseError(ArithmeticException("division by zero"))
  else IO.pure(a / b)

@main def run(): Unit =
  println("--- basic IO ---")
  val hello = IO.delay(println("effect runs here"))
  println("IO created, not yet run")
  hello.unsafeRun()

  println("\n--- map / flatMap ---")
  println(IO.pure(10).map(_ * 2).flatMap(n => IO.pure(n + 1)).unsafeRun())

  println("\n--- for-comprehension ---")
  greetPlain("Alice")
  greet("Alice").unsafeRun()

  println("\n--- attempt ---")
  println(safeDivide(10, 2).attempt.unsafeRun())
  println(safeDivide(10, 0).attempt.unsafeRun())

  println("\n--- handleError ---")
  println(safeDivide(10, 0).handleError(_ => -1).unsafeRun())

  println("\n--- sequence ---")
  println(IO.sequence(List(1, 2, 3).map(n => IO.pure(n * 10))).unsafeRun())

  println("\n--- referential transparency ---")
  val effect = IO.delay { scala.util.Random.nextInt(100) }
  val pair   = for a <- effect; b <- effect yield (a, b)
  println(pair.unsafeRun())
