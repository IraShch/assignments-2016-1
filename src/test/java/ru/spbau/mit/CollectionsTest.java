package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.spbau.mit.Function1Test.CLASS_NAME;
import static ru.spbau.mit.PredicateTest.IS_EVEN;
import static org.junit.Assert.assertEquals;

public class CollectionsTest {

    public static final Function2<Integer, Integer, Integer> DIFF = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer t1, Integer t2) {
            return t1 - t2;
        }
    };

    private static final Integer TWO = 2;
    private static final Integer THREE = 3;
    private static final Integer MSIX = -6;

    @Test
    public void testMap() throws Exception {
        List<Integer> numbers = Arrays.asList(1, TWO, THREE);
        Collection<String> results = Collections.map(CLASS_NAME, numbers);
        assertEquals(numbers.size(), results.size());
        for (String result : results) {
            assertEquals("java.lang.Integer", result);
        }
    }

    @Test
    public void testFilter() throws Exception {
        List<Integer> numbers = Arrays.asList(1, TWO, THREE);
        Collection<Integer> results = Collections.filter(IS_EVEN, numbers);
        assertEquals(1, results.size());
        for (Integer result : results) {
            assertEquals(TWO, result);
        }
    }

    @Test
    public void testTakeWhile() throws Exception {
        List<Integer> numbers = Arrays.asList(1, TWO, THREE);
        Collection<Integer> results = Collections.takeWhile(IS_EVEN, numbers);
        assertEquals(0, results.size());
    }

    @Test
    public void testTakeUnless() throws Exception {
        List<Integer> numbers = Arrays.asList(1, TWO, THREE);
        Collection<Integer> results = Collections.takeUnless(IS_EVEN, numbers);
        assertEquals(1, results.size());
    }

    @Test
    public void testFoldl() throws Exception {
        List<Integer> numbers = Arrays.asList(1, TWO, THREE);
        assertEquals(MSIX, Collections.foldl(DIFF, 0, numbers));

    }

    @Test
    public void testFoldr() throws Exception {
        List<Integer> numbers = Arrays.asList(1, TWO, THREE);
        assertEquals(TWO, Collections.foldr(DIFF, 0, numbers));

    }
}
