import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Streams {

    record Person(String name, int age) {}

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int[] array = {10, 20, 30, 40, 50};
        List<Person> people = List.of(
            new Person("Alice", 30),
            new Person("Bob", 17),
            new Person("Carol", 25),
            new Person("Dave", 15)
        );

        // FILTER
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        System.out.println("evens: " + evens);

        // MAP
        List<String> strings = numbers.stream()
            .map(n -> "n" + n)
            .toList();
        System.out.println("mapped: " + strings);

        // ARRAY STREAM
        int sum = Arrays.stream(array).sum();
        int[] doubled = Arrays.stream(array).map(n -> n * 2).toArray();
        System.out.println("array sum: " + sum);
        System.out.println("array doubled: " + Arrays.toString(doubled));

        // PREDICATES
        Predicate<Person> isAdult = p -> p.age() >= 18;
        Predicate<Person> nameStartsWithA = p -> p.name().startsWith("A");

        List<String> adultNames = people.stream()
            .filter(isAdult)
            .map(Person::name)
            .toList();
        System.out.println("adults: " + adultNames);

        List<String> adultNamesStartingWithA = people.stream()
            .filter(isAdult.and(nameStartsWithA))
            .map(Person::name)
            .toList();
        System.out.println("adults starting with A: " + adultNamesStartingWithA);

        List<String> minorsOrA = people.stream()
            .filter(isAdult.negate().or(nameStartsWithA))
            .map(Person::name)
            .toList();
        System.out.println("minors or name starts with A: " + minorsOrA);

        // COLLECT TO MAP
        Map<String, Integer> nameToAge = people.stream()
            .collect(Collectors.toMap(Person::name, Person::age));
        System.out.println("name->age map: " + nameToAge);

        Map<Boolean, List<Person>> partitioned = people.stream()
            .collect(Collectors.partitioningBy(isAdult));
        System.out.println("adults: " + partitioned.get(true));
        System.out.println("minors: " + partitioned.get(false));

        // REDUCE
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println("product 1..10: " + product);

        // MISC
        Optional<Person> youngest = people.stream()
            .min(Comparator.comparingInt(Person::age));
        youngest.ifPresent(p -> System.out.println("youngest: " + p));

        long adultCount = people.stream().filter(isAdult).count();
        System.out.println("adult count: " + adultCount);

        String joined = people.stream()
            .map(Person::name)
            .collect(Collectors.joining(", "));
        System.out.println("joined: " + joined);
    }
}
