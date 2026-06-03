// --- REFLECTION (Java reflection in Scala 3) ---

case class Person(name: String, age: Int):
  def greet(): String = s"Hi, I'm $name"

class Counter(var count: Int)

@main def run(): Unit =
  val p = Person("Alice", 30)
  val cls = p.getClass

  println("Fields:")
  cls.getDeclaredFields.foreach { f =>
    f.setAccessible(true)
    println(s"  ${f.getName}: ${f.getType.getSimpleName} = ${f.get(p)}")
  }

  println("\nMethods:")
  cls.getDeclaredMethods
    .filterNot(m => m.isSynthetic || m.getName.contains("$"))
    .foreach(m => println(s"  ${m.getName}(${m.getParameterTypes.map(_.getSimpleName).mkString(", ")}): ${m.getReturnType.getSimpleName}"))

  println("\nInvoking 'greet' dynamically:")
  val greetMethod = cls.getDeclaredMethod("greet")
  println(s"  ${greetMethod.invoke(p)}")

  println("\nMutating 'count' field on Counter:")
  val c = Counter(0)
  val countField = c.getClass.getDeclaredField("count")
  countField.setAccessible(true)
  countField.set(c, 42)
  println(s"  count after set(42) = ${countField.get(c)}")

  println("\nInstantiating Person via Class.forName:")
  val dynClass = Class.forName(cls.getName)
  val ctor = dynClass.getDeclaredConstructors.head
  val dynPerson = ctor.newInstance("Bob", Int.box(25))
  println(s"  created: $dynPerson")
