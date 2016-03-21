package ru.spbau.mit;

public abstract class Function2<T1, T2, R> implements Function<T1, Function<T2, R>> {
    public abstract R apply(T1 t1, T2 t2);

    public Function1<T2, R> apply(final T1 t1) {
        return new Function1<T2, R>() {
            public R apply(T2 t2) {
                return Function2.this.apply(t1, t2);
            }
        };
    }

    public <R2> Function2<T1, T2, R2> compose(final Function<? super R, R2> g) {
        return new Function2<T1, T2, R2>() {
            @Override
            public R2 apply(T1 t1, T2 t2) {
                return g.apply(Function2.this.apply(t1, t2));
            }
        };
    }

    public Function2<Object, T2, R> bind1(final T1 t1) {
        return new Function2<Object, T2, R>() {
            @Override
            public R apply(Object o, T2 t2) {
                return Function2.this.apply(t1, t2);
            }
        };
    }

    public Function2<T1, Object, R> bind2(final T2 t2) {
        return new Function2<T1, Object, R>() {
            @Override
            public R apply(T1 t1, Object o) {
                return Function2.this.apply(t1, t2);
            }
        };
    }

    public Function1<T2, R> curry(T1 t1) {
        return apply(t1);
    }
}
