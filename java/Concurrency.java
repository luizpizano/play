import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Concurrency {

    static int slowSquare(int n) throws InterruptedException {
        Thread.sleep(100);
        return n * n;
    }

    // Explicit Callable implementation (vs lambda)
    static class FactorialCallable implements Callable<Long> {
        private final int n;
        FactorialCallable(int n) { this.n = n; }

        @Override
        public Long call() throws Exception {
            if (n < 0) throw new IllegalArgumentException("negative: " + n);
            long result = 1;
            for (int i = 2; i <= n; i++) result *= i;
            Thread.sleep(50);
            return result;
        }
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

        // ── CALLABLE: explicit class ─────────────────────────────────────
        System.out.println("\n--- Callable (explicit class) ---");
        ExecutorService pool4 = Executors.newFixedThreadPool(3);

        Future<Long> fact5  = pool4.submit(new FactorialCallable(5));
        Future<Long> fact10 = pool4.submit(new FactorialCallable(10));
        Future<Long> factBad = pool4.submit(new FactorialCallable(-1));

        System.out.println("5!  = " + fact5.get());
        System.out.println("10! = " + fact10.get());

        // ExecutionException wraps the exception thrown inside call()
        try {
            factBad.get();
        } catch (ExecutionException e) {
            System.out.println("caught ExecutionException → cause: " + e.getCause().getMessage());
        }

        pool4.shutdown();

        // ── FUTURE: state inspection ─────────────────────────────────────
        System.out.println("\n--- Future state: isDone / isCancelled ---");
        ExecutorService pool5 = Executors.newSingleThreadExecutor();

        Future<String> pending = pool5.submit(() -> { Thread.sleep(400); return "done"; });

        System.out.println("isDone before sleep:  " + pending.isDone());
        System.out.println("isCancelled:          " + pending.isCancelled());
        Thread.sleep(500);
        System.out.println("isDone after sleep:   " + pending.isDone());
        System.out.println("result:               " + pending.get());

        // cancel a running task (mayInterruptIfRunning = true)
        Future<String> toBeCancelled = pool5.submit(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) { /* interrupted */ }
            return "never";
        });
        Thread.sleep(50);
        boolean cancelled = toBeCancelled.cancel(true);
        System.out.println("cancel() returned:    " + cancelled);
        System.out.println("isCancelled:          " + toBeCancelled.isCancelled());

        // get() on a cancelled future throws CancellationException
        try {
            toBeCancelled.get();
        } catch (CancellationException e) {
            System.out.println("caught CancellationException as expected");
        }

        pool5.shutdown();

        // ── FUTURETASK: Callable + Runnable in one ───────────────────────
        System.out.println("\n--- FutureTask ---");
        FutureTask<Integer> ft = new FutureTask<>(() -> {
            Thread.sleep(100);
            return 42;
        });

        // FutureTask implements Runnable — can run on a plain Thread
        Thread t = new Thread(ft);
        t.start();

        System.out.println("FutureTask isDone before join: " + ft.isDone());
        t.join();
        System.out.println("FutureTask result: " + ft.get());
        System.out.println("FutureTask isDone after join:  " + ft.isDone());

        // ── COMPLETABLE FUTURE: chaining ─────────────────────────────────
        System.out.println("\n--- CompletableFuture ---");

        // thenApply: transform result (sync)
        CompletableFuture<Integer> cf = CompletableFuture
            .supplyAsync(() -> 7)
            .thenApply(n -> n * n);
        System.out.println("supplyAsync → thenApply: " + cf.get());

        // thenCompose: flat-map to another CompletableFuture
        CompletableFuture<String> cf2 = CompletableFuture
            .supplyAsync(() -> 5)
            .thenCompose(n -> CompletableFuture.supplyAsync(() -> "square of 5 = " + (n * n)));
        System.out.println("thenCompose: " + cf2.get());

        // thenCombine: merge two independent futures
        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> sum = a.thenCombine(b, Integer::sum);
        System.out.println("thenCombine (10+20): " + sum.get());

        // exceptionally: recover from failure
        CompletableFuture<Integer> failing = CompletableFuture
            .<Integer>supplyAsync(() -> { throw new RuntimeException("boom"); })
            .exceptionally(ex -> { System.out.println("exceptionally: " + ex.getMessage()); return -1; });
        System.out.println("recovered value: " + failing.get());

        // allOf: wait for all to complete
        CompletableFuture<Void> all = CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> System.out.println("allOf task-1")),
            CompletableFuture.runAsync(() -> System.out.println("allOf task-2")),
            CompletableFuture.runAsync(() -> System.out.println("allOf task-3"))
        );
        all.get();
        System.out.println("allOf complete");

        // anyOf: resolves with the first completed value
        CompletableFuture<Object> any = CompletableFuture.anyOf(
            CompletableFuture.supplyAsync(() -> { try { Thread.sleep(300); } catch (Exception e) {} return "slow"; }),
            CompletableFuture.supplyAsync(() -> { try { Thread.sleep(10);  } catch (Exception e) {} return "fast"; })
        );
        System.out.println("anyOf winner: " + any.get());
    }
}
