package ru.spbau.mit;

public class ThreadPoolThread extends Thread {
    private BlockingQueue<LightFutureImpl<?>> taskQueue;
    private ThreadPoolImpl threadPool;

    public ThreadPoolThread(BlockingQueue<LightFutureImpl<?>> queue, ThreadPoolImpl threadPool) {
        this.taskQueue = queue;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        try {
            while (true) {
                LightFutureImpl task = taskQueue.take();
                task.execute();

                if (threadPool.isShutdown() && taskQueue.size() == 0) {
                    this.interrupt();
                }
            }
        } catch (InterruptedException e) { }
    }
}
