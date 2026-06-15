final class Reader[R, A](val run: R => A):
  def map[B](f: A => B): Reader[R, B]               = Reader(r => f(run(r)))
  def flatMap[B](f: A => Reader[R, B]): Reader[R, B] = Reader(r => f(run(r)).run(r))

object Reader:
  def pure[R, A](a: A): Reader[R, A]                         = Reader(_ => a)
  def ask[R]: Reader[R, R]                                    = Reader(identity)
  def asks[R, A](f: R => A): Reader[R, A]                    = Reader(f)
  def local[R, A](f: R => R)(ra: Reader[R, A]): Reader[R, A] = Reader(r => ra.run(f(r)))

case class Config(host: String, port: Int, db: String)

def dbHost: Reader[Config, String] = Reader.asks(_.host)
def dbPort: Reader[Config, Int]    = Reader.asks(_.port)
def dbName: Reader[Config, String] = Reader.asks(_.db)

def connStr: Reader[Config, String] =
  for h <- dbHost; p <- dbPort yield s"$h:$p"

def jdbcUrl: Reader[Config, String] =
  for conn <- connStr; db <- dbName yield s"jdbc:postgresql://$conn/$db"
