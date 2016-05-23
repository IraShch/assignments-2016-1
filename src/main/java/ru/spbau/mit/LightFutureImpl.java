package ru.spbau.mit;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightFutureImpl<R> implements LightFuture<R> {
    private final Supplier<R> supplier;
    private final ThreadPoolImpl pool;

    private volatile R result = null;
    private volatile boolean isReady = false;
    private volatile Throwable exc = null;

    private List<LightFuture<?>> children = new LinkedList<>();

    protected LightFutureImpl(Supplier<R> supplier, ThreadPoolImpl pool) {
        this.supplier = supplier;
        this.pool = pool;
    }

    public synchronized void execute() {
        if (!isReady) {
            try {
                result = supplier.get();
            } catch (Exception e) {
                exc = e;
            }
            isReady = true;
            if (!children.isEmpty()) {
                for (LightFuture<?> child : children) {
                    pool.putTask((LightFutureImpl<?>) child);
                }
            }
            notifyAll();
        }
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public R get() throws LightExecutionException, InterruptedException {
        synchronized (this) {
            while (!isReady) {
                wait();
            }
        }
        if (exc != null) {
            throw new LightExecutionException(exc);
        }

        return result;
    }

    @Override
    public <U> LightFuture<U> thenApply(Function<? super R, ? extends U> f) {
        Supplier<U> supplier1 = () -> {
            R intermediateResult = null;
            try {
                intermediateResult = LightFutureImpl.this.get();
            } catch (Exception e) {
                throw new RuntimeException("One of the futures in chain resulted in exception", e);
            }
            return f.apply(intermediateResult);
        };
        LightFutureImpl<U> task = new LightFutureImpl<>(supplier1, pool);
        if (isReady()) {
            pool.submit(task);
        }
        LightFuture<U> nextFuture = task;
        children.add(nextFuture);
        return nextFuture;
    }
}
