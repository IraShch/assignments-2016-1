package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {
    public static final String THREAD_NAME_PREFIX = "PoolThread";
    private BlockingQueue<LightFutureImpl<?>> taskQueue = new BlockingQueue<>();
    private boolean isShutdown = false;

    public ThreadPoolImpl(int n) {
        List<Thread> threads = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(this::runTask);
            thread.setName(THREAD_NAME_PREFIX + i);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public <R> LightFutureImpl<R> submit(Supplier<R> supplier) {
        LightFutureImpl<R> task = new LightFutureImpl<>(supplier, this);
        submit(task);
        return task;
    }

    public <R> void submit(LightFutureImpl<R> task) {
        taskQueue.put(task);
    }

    @Override
    public void shutdown() {
        isShutdown = true;
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    protected void putTask(LightFutureImpl<?> item) {
        taskQueue.put(item);
    }

    private void runTask() {
        try {
            while (true) {
                LightFutureImpl task = taskQueue.take();
                task.execute();

                if (this.isShutdown() && taskQueue.size() == 0) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (InterruptedException e) {

        }
    }
}
