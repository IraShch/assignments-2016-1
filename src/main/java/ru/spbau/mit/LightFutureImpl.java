package ru.spbau.mit;

import java.util.function.Function;
import java.util.function.Supplier;

public class LightFutureImpl<R> implements LightFuture<R> {
    private final Supplier<R> supplier;
    private final ThreadPoolImpl pool;

    private volatile R result = null;
    private volatile boolean isReady = false;
    private volatile Throwable exc = null;

    private LightFuture<?> child = null;

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
            if (child != null) {
                pool.putTask((LightFutureImpl<?>) child);
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
        LightFuture<U> nextFuture = pool.submitFromParent(() -> {
            R intermediateResult = null;
            try {
                intermediateResult = LightFutureImpl.this.get();
            } catch (Exception e) {
                throw new RuntimeException("One of the futures in chain resulted in exception", e);
            }
            return f.apply(intermediateResult);
        }, this);
        child = nextFuture;
        return nextFuture;
    }
}
