package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ThreadPoolTests {

    private static final int TASK_N = 30;
    private static final int SLEEP_TIME = 500;

    private class CountSum implements Supplier<Integer> {

        private final int n;

        CountSum(int n) {
            this.n = n;
        }

        @Override
        public Integer get() {
            return IntStream.range(1, n).sum();
        }
    }

    private class SleepingTask implements Supplier<Integer> {

        @Override
        public Integer get() {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) { }
            return 0;
        }
    }


    @Test
    public void verySimpleTest() throws LightExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(1);
        LightFuture<Integer> one = threadPool.submit(() -> 1);
        LightFuture<Integer> zero = threadPool.submit(() -> 0);

        assertEquals(1, (int) one.get());
        assertEquals(0, (int) zero.get());

        threadPool.shutdown();
    }

    @Test
    public void simpleTest() throws LightExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(TASK_N / 2);

        LightFuture<Integer> future = threadPool.submit(new CountSum(TASK_N));
        Integer expected = IntStream.range(1, TASK_N).sum();
        assertEquals(expected, future.get());

        List<LightFuture<Integer>> results = new LinkedList<>();
        for (int i = 1; i < TASK_N; i++) {
            results.add(threadPool.submit(new CountSum(i)));
        }
        for (int i = 1; i < TASK_N; i++) {
            expected = IntStream.range(1, i).sum();
            assertEquals(expected, results.get(i - 1).get());
        }

        threadPool.shutdown();

    }

    @Test
    public void countThreads() throws LightExecutionException, InterruptedException, BrokenBarrierException {
        ThreadPool threadPool = new ThreadPoolImpl(TASK_N);

        List<LightFuture<Integer>> results = new LinkedList<>();
        List<Boolean> startFlags = new ArrayList<>(TASK_N);
        CyclicBarrier barrier = new CyclicBarrier(TASK_N + 1);

        for (int i = 0; i < TASK_N; i++) {
            int currentIndex = i;
            startFlags.add(false);
            results.add(threadPool.submit(() -> {
                startFlags.set(currentIndex, true);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                return null;
            }));
        }

        while (startFlags.stream().anyMatch((it) -> it.equals(false))) {
            Thread.sleep(0);
        }

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Set<String> poolThreads = threadSet.stream().map(Thread::getName).
                filter((it) -> it.startsWith(ThreadPoolImpl.THREAD_NAME_PREFIX)).collect(Collectors.toSet());

        assertEquals(TASK_N, poolThreads.size());

        barrier.await();

        threadPool.shutdown();
    }

    @Test(expected = LightExecutionException.class)
    public void testException() throws LightExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(1);
        LightFuture<Integer> result = threadPool.submit(() -> {
            throw new ArrayIndexOutOfBoundsException();
        });
        result.get();
    }

    @Test
    public void testThenApply() throws LightExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(TASK_N);

        LightFuture<StringBuilder> result = threadPool.submit(() -> {
            StringBuilder ini = new StringBuilder();
            ini.append("a");
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ini.append("a");
            System.out.println("Parent ready");
            return ini;
        });

        LightFuture<StringBuilder> firstChild = result.thenApply(x -> {
            System.out.println("First child started");
            StringBuilder res = new StringBuilder(x);
            res.append("b");
            return res;
        });

        LightFuture<StringBuilder> secondChild = result.thenApply(x -> {
            System.out.println("Second child started");
            StringBuilder res = new StringBuilder(x);
            res.append("c");
            return res;
        });

        LightFuture<StringBuilder> grandson = firstChild.thenApply(x -> {
            System.out.println("Grandson started");
            StringBuilder res = new StringBuilder(x);
            res.append("d");
            return res;
        });
        assertFalse(firstChild.isReady());
        assertFalse(grandson.isReady());
        assertFalse(secondChild.isReady());

        Thread.sleep(SLEEP_TIME * 2);
        assertEquals("aa", result.get().toString());
        assertEquals("aab", firstChild.get().toString());
        assertEquals("aac", secondChild.get().toString());
        assertEquals("aabd", grandson.get().toString());

        threadPool.shutdown();
    }

    @Test(expected = LightExecutionException.class)
    public void testThenApplyException() throws LightExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(1);

        LightFuture<Integer> result = threadPool.submit(() -> {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        });

        LightFuture<Integer> nextResult = result.thenApply(x -> {
            throw new ArrayIndexOutOfBoundsException();
        });
        assertFalse(result.isReady());
        assertFalse(nextResult.isReady());

        Thread.sleep(SLEEP_TIME * 2);
        assertEquals(0, (int) result.get());
        assertEquals("0", nextResult.get().toString());
    }
}
