final class IO[A](val unsafeRun: () => A):
  def map[B](f: A => B): IO[B]       = IO(() => f(unsafeRun()))
  def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(unsafeRun()).unsafeRun())

object IO:
  def pure[A](a: A): IO[A]     = IO(() => a)
  def delay[A](a: => A): IO[A] = IO(() => a)

def greet(name: String): IO[Unit] =
  for
    _ <- IO.delay(println(s"Hello, $name!"))
    _ <- IO.delay(println(s"${name.length} chars in name"))
  yield ()

@main def run(): Unit =
  println("--- basic IO ---")
  val hello = IO.delay(println("effect runs here"))
  println("IO created, not yet run")
  hello.unsafeRun()

  println("\n--- map / flatMap ---")
  println(IO.pure(10).map(_ * 2).flatMap(n => IO.pure(n + 1)).unsafeRun())

  println("\n--- for-comprehension ---")
  greet("Alice").unsafeRun()
