// --- REFLECTION (Java reflection in Scala 3) ---

case class Person(name: String, age: Int):
  def greet(): String = s"Hi, I'm $name"

class Counter(var count: Int)

@main def run(): Unit =
  val p = Person("Alice", 30)
  val cls = p.getClass

  // Inspect fields declared on the class
  println("Fields:")
  cls.getDeclaredFields.foreach { f =>
    f.setAccessible(true)
    println(s"  ${f.getName}: ${f.getType.getSimpleName} = ${f.get(p)}")
  }

  // Inspect methods (filter out synthetic/Object noise)
  println("\nMethods:")
  cls.getDeclaredMethods
    .filterNot(m => m.isSynthetic || m.getName.contains("$"))
    .foreach(m => println(s"  ${m.getName}(${m.getParameterTypes.map(_.getSimpleName).mkString(", ")}): ${m.getReturnType.getSimpleName}"))

  // Invoke a method by name at runtime
  println("\nInvoking 'greet' dynamically:")
  val greetMethod = cls.getDeclaredMethod("greet")
  println(s"  ${greetMethod.invoke(p)}")

  // Read and mutate a field dynamically
  println("\nMutating 'count' field on Counter:")
  val c = Counter(0)
  val countField = c.getClass.getDeclaredField("count")
  countField.setAccessible(true)
  countField.set(c, 42)
  println(s"  count after set(42) = ${countField.get(c)}")

  // Instantiate a class by name at runtime
  println("\nInstantiating Person via Class.forName:")
  val dynClass = Class.forName(cls.getName)
  val ctor = dynClass.getDeclaredConstructors.head
  val dynPerson = ctor.newInstance("Bob", Int.box(25))
  println(s"  created: $dynPerson")
