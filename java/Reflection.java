import java.lang.reflect.*;

public class Reflection {

    static class Person {
        public String name;
        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public String greet() { return "Hi, I'm " + name; }
        private int getAge() { return age; }

        @Override public String toString() { return name + "/" + age; }
    }

    public static void main(String[] args) throws Exception {
        Class<?> c = Person.class;

        // READ
        for (Field f : c.getDeclaredFields()) System.out.println("field: " + f);
        for (Method m : c.getDeclaredMethods()) System.out.println("method: " + m);
        for (Constructor<?> k : c.getDeclaredConstructors()) System.out.println("contructor: " + k);

        Person p = new Person("Alice", 30);
        for (Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(f.getName() + "=" + f.get(p));
        }

        // INVOKE
        System.out.println(c.getMethod("greet").invoke(p));

        Method getAge = c.getDeclaredMethod("getAge");
        getAge.setAccessible(true);
        System.out.println(getAge.invoke(p));

        Person p2 = (Person) c.getDeclaredConstructor(String.class, int.class).newInstance("Bob", 25);
        System.out.println(p2);

        // MODIFY
        System.out.println("before: " + p);
        c.getDeclaredField("name").set(p, "Alice2");
        Field age = c.getDeclaredField("age");
        age.setAccessible(true);
        age.set(p, 31);
        System.out.println("after: " + p);
    }
}
