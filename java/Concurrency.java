import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Concurrency {

    static int slowSquare(int n) throws InterruptedException {
        Thread.sleep(100);
        return n * n;
    }

    public static void main(String[] args) throws Exception {
        // FIXED THREAD POOL
        ExecutorService pool = Executors.newFixedThreadPool(3);

        Future<Integer> f1 = pool.submit(() -> slowSquare(4));
        Future<Integer> f2 = pool.submit(() -> slowSquare(5));
        Future<Integer> f3 = pool.submit(() -> slowSquare(6));

        System.out.println("4^2=" + f1.get());
        System.out.println("5^2=" + f2.get());
        System.out.println("6^2=" + f3.get());

        pool.shutdown();

        // CALLABLE + invokeAll
        ExecutorService pool2 = Executors.newFixedThreadPool(4);

        List<Callable<String>> tasks = List.of(
            () -> { Thread.sleep(50);  return "task-A"; },
            () -> { Thread.sleep(200); return "task-B"; },
            () -> { Thread.sleep(10);  return "task-C"; }
        );

        List<Future<String>> results = pool2.invokeAll(tasks);
        for (Future<String> r : results) System.out.println("done: " + r.get());

        // invokeAny: returns the first to finish
        String first = pool2.invokeAny(tasks);
        System.out.println("first: " + first);

        pool2.shutdown();

        // SCHEDULED EXECUTOR
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger count = new AtomicInteger();

        ScheduledFuture<?> periodic = scheduler.scheduleAtFixedRate(
            () -> System.out.println("tick #" + count.incrementAndGet()),
            0, 150, TimeUnit.MILLISECONDS
        );

        Thread.sleep(500);
        periodic.cancel(false);
        scheduler.shutdown();

        // TIMEOUT via Future.get
        ExecutorService pool3 = Executors.newSingleThreadExecutor();
        Future<String> slow = pool3.submit(() -> { Thread.sleep(2000); return "late"; });

        try {
            slow.get(300, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println("timed out — cancelling");
            slow.cancel(true);
        }

        pool3.shutdown();
    }
}
