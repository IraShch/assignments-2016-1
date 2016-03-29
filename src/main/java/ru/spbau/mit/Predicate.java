package ru.spbau.mit;

public abstract class Predicate<T> extends Function1<T, Boolean> {

    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        public Boolean apply(Object o) {
            return true;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        public Boolean apply(Object o) {
            return false;
        }
    };

    public Predicate<T> or(final Predicate<? super T> predicate) {
        return new Predicate<T>() {
            public Boolean apply(T t) {
                return Predicate.this.apply(t) || predicate.apply(t);
            }
        };
    }

    public Predicate<T> and(final Predicate<? super T> predicate) {
        return new Predicate<T>() {
            public Boolean apply(T t) {
                return Predicate.this.apply(t) && predicate.apply(t);
            }
        };
    }

    public Predicate<T> not() {
        return new Predicate<T>() {
            public Boolean apply(T t) {
                return !Predicate.this.apply(t);
            }
        };
    }

}
