package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FoldlExtendsTest {

    public static final Function2<IntContainer, IntContainer, IntContainerChild> SUM =
            new Function2<IntContainer, IntContainer, IntContainerChild>() {
                @Override
                public IntContainerChild apply(IntContainer t1, IntContainer t2) {
                    return new IntContainerChild(t1.getInteger() + t2.getInteger());
                }
            };

    private static final Integer NINE = 9;
    private static final Integer THREE = 3;

    @Test
    public void testFoldlExtend() throws Exception {
        IntContainer ic = new IntContainer(THREE);
        List<IntContainer> numbers = Arrays.asList(ic, ic, ic);
        assertEquals(new IntContainerChild(NINE), Collections.foldl(SUM, new IntContainer(0), numbers));
    }


    public static class IntContainer {
        private final Integer integer;

        public IntContainer(Integer integer) {
            this.integer = integer;
        }

        public Integer getInteger() {
            return integer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            IntContainer ic = (IntContainer) o;
            return integer != null ? integer.equals(ic.integer) : ic.integer == null;
        }

        @Override
        public int hashCode() {
            return integer != null ? integer.hashCode() : 0;
        }
    }

    public static class IntContainerChild extends IntContainer {
        public IntContainerChild(Integer integer) {
            super(integer);
        }

        /**/
    }
}
