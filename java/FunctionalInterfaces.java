import java.util.*;
import java.util.function.*;

public class FunctionalInterfaces {

    public static void main(String[] args) {
        // Function<T, R> — recebe T, retorna R
        Function<String, Integer> length = s -> s.length();
        System.out.println(length.apply("hello"));          // 5

        // BiFunction<T, U, R> — recebe T e U, retorna R
        BiFunction<String, String, String> concat = (a, b) -> a + b;
        System.out.println(concat.apply("foo", "bar"));     // foobar

        // Predicate<T> — recebe T, retorna boolean
        Predicate<Integer> isEven = n -> n % 2 == 0;
        System.out.println(isEven.test(4));                 // true
        System.out.println(isEven.test(7));                 // false

        // Consumer<T> — recebe T, não retorna nada
        Consumer<String> print = s -> System.out.println(">>> " + s);
        print.accept("hello consumer");

        // Supplier<T> — não recebe nada, retorna T
        Supplier<List<String>> newList = ArrayList::new;
        List<String> list = newList.get();
        list.add("a");
        System.out.println(list);                           // [a]

        // UnaryOperator<T> — Function onde T == R
        UnaryOperator<String> upper = String::toUpperCase;
        System.out.println(upper.apply("world"));           // WORLD

        // BinaryOperator<T> — BiFunction onde T == U == R
        BinaryOperator<Integer> sum = Integer::sum;
        System.out.println(sum.apply(3, 4));                // 7

        // Composition: andThen / compose
        Function<Integer, Integer> times2 = n -> n * 2;
        Function<Integer, Integer> plus3  = n -> n + 3;
        System.out.println(times2.andThen(plus3).apply(5)); // (5*2)+3 = 13
        System.out.println(times2.compose(plus3).apply(5)); // (5+3)*2 = 16

        // Predicate: and / or / negate
        Predicate<Integer> isPositive = n -> n > 0;
        System.out.println(isEven.and(isPositive).test(4)); // true
        System.out.println(isEven.or(isPositive).test(3));  // true
        System.out.println(isEven.negate().test(4));        // false

        // Consumer: andThen
        Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());
        print.andThen(printUpper).accept("chained");
    }
}
