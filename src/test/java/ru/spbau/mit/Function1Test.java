package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {

    private static final int THREE = 3;

    @Test
    public void testSimple() throws Exception {
        assertEquals(THREE * 2, (int) MULTIPLY_BY_2.apply(THREE));
    }

    @Test
    public void testCompose() throws Exception {
        Function<String, Boolean> lengthIsOdd = LENGTH.compose(PredicateTest.IS_ODD);
        assertFalse(lengthIsOdd.apply(""));
        assertTrue(lengthIsOdd.apply(" "));
    }

    @Test
    public void testComposeWithSuperType() throws Exception {
        Function<Integer, String> fun = new Id<Integer>().compose(CLASS_NAME);
        assertEquals("java.lang.Integer", fun.apply(THREE));
    }


    static final Function1<Integer, Integer> MULTIPLY_BY_2 = new Function1<Integer, Integer>() {
        public Integer apply(Integer number) {
            return number * 2;
        }
    };

    static final Function1<String, Integer> LENGTH = new Function1<String, Integer>() {
        public Integer apply(String s) {
            return s.length();
        }
    };

    static final Function1<Object, String> CLASS_NAME = new Function1<Object, String>() {
        public String apply(Object o) {
            return o.getClass().getName();
        }
    };

    static final Function1<Object, Object> GEN_ID = new Id<>();
}
