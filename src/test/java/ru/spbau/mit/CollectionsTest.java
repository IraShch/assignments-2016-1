package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.spbau.mit.Function1Test.CLASS_NAME;
import static org.junit.Assert.assertEquals;

public class CollectionsTest {

    private static final int THREE = 3;

    @Test
    public void testMap() throws Exception {
        List<Integer> numbers = Arrays.asList(1, 2, THREE);
        Collection<String> results = Collections.map(CLASS_NAME, numbers);
        assertEquals(numbers.size(), results.size());
        for (String result : results) {
            assertEquals("java.lang.Integer", result);
        }
    }
}
