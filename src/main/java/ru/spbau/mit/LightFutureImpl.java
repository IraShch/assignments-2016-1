package ru.spbau.mit;

import java.util.function.Function;
import java.util.function.Supplier;

public class LightFutureImpl<R> implements LightFuture<R> {
    private final Supplier<R> supplier;
    private final ThreadPool pool;

    private volatile R result = null;
    private volatile boolean isReady = false;
    private volatile Throwable exc = null;

    public LightFutureImpl(Supplier<R> supplier, ThreadPool pool) {
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
        return pool.submit(() -> {
            R intermediateResult = null;
            try {
                intermediateResult = LightFutureImpl.this.get();
            } catch (Exception e) {
                throw new RuntimeException("One of the futures in chain resulted in exception", e);
            }
            return f.apply(intermediateResult);
        });
    }
}
