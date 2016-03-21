package ru.spbau.mit;

public abstract class Function1<T, R> implements Function<T, R> {
    public <R2> Function1<T, R2> compose(final Function<? super R, R2> g) {
        return new Function1<T, R2>() {
            public R2 apply(T t) {
                return g.apply(Function1.this.apply(t));
            }
        };
    }
}
