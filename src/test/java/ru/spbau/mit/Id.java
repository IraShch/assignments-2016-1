package ru.spbau.mit;

public final class Id<T> extends Function1<T, T> {
    public T apply(T t) {
        return t;
    }
}
