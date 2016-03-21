package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Collections {
    private Collections() {
    }

    public static <T, R> Collection<R> map(Function<T, ? extends R> f, Iterable<? extends T> collection) {
        List<R> result = new ArrayList<>();
        for (T elem : collection) {
            result.add(f.apply(elem));
        }
        return result;
    }

    public static <T> Collection<T> filter(Predicate<T> p, Iterable<? extends T> collection) {
        List<T> result = new ArrayList<>();
        for (T elem : collection) {
            if (p.apply(elem)) {
                result.add(elem);
            }
        }
        return result;
    }

    public static <T> Collection<T> takeWhile(Predicate<T> p, Iterable<? extends T> collection) {
        List<T> result = new ArrayList<>();
        for (T elem : collection) {
            if (!p.apply(elem)) {
                break;
            }
            result.add(elem);
        }
        return result;
    }

    public static <T> Collection<T> takeUnless(Predicate<T> p, Iterable<? extends T> collection) {
        return takeWhile(p.not(), collection);
    }

    public static <T, R> R foldl(Function2<? super R, T, ? extends R> f, R start,
                                 Iterable<? extends T> collection) {
        R result = start;
        for (T elem : collection) {
            result = f.apply(result, elem);
        }
        return result;
    }

    public static <T, R> R foldr(Function2<T, ? super R, ? extends R> f,
                                 R start, Iterable<? extends T> collection) {
        R result = start;
        List<T> copyCollection = new ArrayList<>();
        for (T elem : collection) {
            copyCollection.add(elem);
        }

        for (int i = copyCollection.size() - 1; i >= 0; i--) {
            result = f.apply(copyCollection.get(i), result);
        }
        return result;
    }

}
