package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {

    private static final int THREE = 3;
    private static final int FOUR = 4;

    @Test
    public void testSimple() throws Exception {
        assertTrue(IS_EVEN.apply(FOUR));
        assertTrue(IS_ODD.apply(THREE));
    }

    @Test
    public void testAnd() throws Exception {
        Integer a = THREE;
        assertEquals(false, IS_ODD.and(Predicate.ALWAYS_FALSE).apply(a));
        assertEquals(true, IS_ODD.and(Predicate.ALWAYS_TRUE).apply(a));
        assertEquals(false, IS_EVEN.and(Predicate.ALWAYS_TRUE).apply(a));
        assertEquals(false, IS_EVEN.and(THROWING_EXCEPTION).apply(a));
    }

    @Test
    public void testOr() throws Exception {
        Integer a = THREE;
        assertEquals(false, IS_EVEN.or(Predicate.ALWAYS_FALSE).apply(a));
        assertEquals(true, IS_ODD.or(Predicate.ALWAYS_FALSE).apply(a));
        assertEquals(true, IS_ODD.or(THROWING_EXCEPTION).apply(a));
        assertEquals(true, IS_EVEN.or(Predicate.ALWAYS_TRUE).apply(a));
    }

    @Test
    public void testNot() throws Exception {
        assertFalse(Predicate.ALWAYS_TRUE.not().apply(null));
        assertTrue(Predicate.ALWAYS_FALSE.not().apply(null));
    }

    static final Predicate<Integer> IS_EVEN = new Predicate<Integer>() {
        public Boolean apply(Integer number) {
            return number % 2 == 0;
        }
    };

    static final Predicate<Integer> IS_ODD = new Predicate<Integer>() {
        public Boolean apply(Integer number) {
            return !IS_EVEN.apply(number);
        }
    };

    static final Predicate<Object> THROWING_EXCEPTION = new Predicate<Object>() {
        public Boolean apply(Object o) {
            throw new RuntimeException();
        }
    };
}
