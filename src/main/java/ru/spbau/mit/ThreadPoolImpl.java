package ru.spbau.mit;

import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {
    private BlockingQueue<LightFutureImpl<?>> taskQueue = new BlockingQueue<>();
    private boolean isShutdown = false;

    public ThreadPoolImpl(int n) {
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new ThreadPoolThread(taskQueue, this);
            threads[i].start();
        }
    }

    @Override
    public <R> LightFutureImpl<R> submit(Supplier<R> supplier) {
        LightFutureImpl<R> task = new LightFutureImpl<>(supplier, this);
        taskQueue.put(task);
        return task;
    }

    @Override
    public void shutdown() {
        isShutdown = true;
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    protected <R> LightFutureImpl<R> submitFromParent(Supplier<R> supplier, LightFuture<?> parent) {
        LightFutureImpl<R> task = new LightFutureImpl<>(supplier, this);
        if (parent.isReady()) {
            taskQueue.put(task);
        }
        return task;
    }

    protected void putTask(LightFutureImpl<?> item) {
        taskQueue.put(item);
    }
}
