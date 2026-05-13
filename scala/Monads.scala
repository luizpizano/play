import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.util.{Try, Success, Failure}

// --- OPTION ---
def divide(a: Int, b: Int): Option[Int] =
  if b == 0 then None else Some(a / b)

def userCity(users: Map[Int, String], cities: Map[String, String], id: Int): Option[String] =
  users.get(id).flatMap(cities.get)

// --- TRY ---
def parseInt(s: String): Try[Int] = Try(s.toInt)

def safeDivide(a: Int, b: Int): Try[Int] = Try(a / b)

// --- EITHER ---
def validateAge(age: Int): Either[String, Int] =
  if age < 0 then Left("age cannot be negative")
  else if age > 150 then Left("age unrealistically large")
  else Right(age)

def validateName(name: String): Either[String, String] =
  if name.isBlank then Left("name cannot be blank") else Right(name.trim)

case class User(name: String, age: Int)

def createUser(name: String, age: Int): Either[String, User] =
  for
    n <- validateName(name)
    a <- validateAge(age)
  yield User(n, a)

// --- FUTURE ---
def fetchUser(id: Int): Future[String] =
  if id > 0 then Future.successful(s"user-$id")
  else Future.failed(RuntimeException("invalid id"))

def fetchScore(user: String): Future[Int] =
  Future.successful(user.length * 10)

// --- LIST ---
def pairs(xs: List[Int], ys: List[Int]): List[(Int, Int)] =
  xs.flatMap(x => ys.map(y => (x, y)))

// --- MAP ---
val scores: Map[String, Int]  = Map("alice" -> 90, "bob" -> 75, "carol" -> 85)
val bonuses: Map[String, Int] = Map("alice" -> 10, "carol" -> 5)

def totalScore(name: String): Option[Int] =
  for
    base  <- scores.get(name)
    bonus <- bonuses.get(name)
  yield base + bonus

@main def run(): Unit =
  // OPTION
  println(divide(10, 2))
  println(divide(10, 0))
  println(divide(10, 2).map(_ * 3))
  println(divide(10, 0).getOrElse(-1))
  val users  = Map(1 -> "alice", 2 -> "bob")
  val cities = Map("alice" -> "NYC", "bob" -> "LA")
  println(userCity(users, cities, 1))
  println(userCity(users, cities, 99))
  val optChain = for
    a <- divide(20, 4)
    b <- divide(a, 1)
  yield a + b
  println(optChain)

  // TRY
  println(parseInt("42"))
  println(parseInt("oops"))
  println(safeDivide(10, 2))
  println(safeDivide(10, 0))
  println(parseInt("10").map(_ * 2))
  println(parseInt("bad").recover { case _: NumberFormatException => -1 })
  val tryChain = for
    x <- parseInt("10")
    y <- parseInt("20")
  yield x + y
  println(tryChain)

  // EITHER
  println(validateAge(25))
  println(validateAge(-1))
  println(createUser("Alice", 30))
  println(createUser("", 30))
  println(createUser("Bob", -5))
  println(validateAge(20).map(_ * 2))
  println(validateAge(-1).fold(e => s"error: $e", a => s"ok: $a"))

  // FUTURE
  println(Await.result(fetchUser(1), 1.second))
  val pipeline = fetchUser(3).flatMap(fetchScore)
  println(Await.result(pipeline, 1.second))
  val recovered = fetchUser(-1).recover { case ex => s"fallback: ${ex.getMessage}" }
  println(Await.result(recovered, 1.second))
  val mapped = for
    u <- fetchUser(2)
    s <- fetchScore(u)
  yield s"$u scored $s"
  println(Await.result(mapped, 1.second))

  // LIST
  println(List(1, 2, 3).map(_ * 2))
  println(List(1, 2, 3).flatMap(x => List(x, -x)))
  println(pairs(List(1, 2), List(10, 20)))
  val combos = for
    x <- List("a", "b")
    y <- List(1, 2)
  yield s"$x$y"
  println(combos)
  println(List(List(1, 2), List(3, 4)).flatten)

  // MAP
  println(scores.get("alice"))
  println(scores.get("dave"))
  println(scores.map((k, v) => k -> (v + 5)))
  println(scores.filter((_, v) => v >= 85))
  println(totalScore("alice"))
  println(totalScore("bob"))
