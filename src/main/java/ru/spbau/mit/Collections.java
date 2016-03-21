package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Collections {

    private Collections() {}

    public static <T, R> Collection<R> map(Function<T, R> f, Iterable<? extends T> collection) {
        List<R> result = new ArrayList<>();
        for (T elem : collection) {
            result.add(f.apply(elem));
        }
        return result;
    }
}
