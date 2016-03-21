package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Function2Test {

    private static final int THREE = 3;

    @Test
    public void testSimple() throws Exception {
        assertEquals(false, LENGTH_PLUS_N_IS_ODD.apply("lol", 1));
        assertEquals(true, LENGTH_PLUS_N_IS_ODD.apply("", 1));
    }

    @Test
    public void testComposeWithSuperType() throws Exception {
        assertEquals("true", LENGTH_PLUS_N_IS_ODD.compose(Function1Test.GEN_ID).apply("", 1).toString());
    }

    @Test
    public void testBind1() throws Exception {
        assertEquals(1, GEN_FST.bind1(1).apply(2, 2));
    }

    @Test
    public void testBind2() throws Exception {
        assertEquals(THREE, GEN_SND.bind2(THREE).apply(2, 2));
    }

    @Test
    public void testCurry() throws Exception {
        Function<Integer, Boolean> curried = LENGTH_PLUS_N_IS_ODD.curry("lol");
        assertEquals(true, curried.apply(2));
    }

    static final Function2<String, Integer, Boolean> LENGTH_PLUS_N_IS_ODD =
            new Function2<String, Integer, Boolean>() {
        @Override
        public Boolean apply(String s, Integer number) {
            return (s.length() + number) % 2 != 0;
        }
    };

    static final Function2<Object, Object, Object> GEN_FST = new Function2<Object, Object, Object>() {
        @Override
        public Object apply(Object o, Object o2) {
            return o;
        }
    };

    static final Function2<Object, Object, Object> GEN_SND = new Function2<Object, Object, Object>() {
        @Override
        public Object apply(Object o, Object o2) {
            return o2;
        }
    };
}
