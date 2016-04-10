package ru.spbau.mit;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> fileNames = Arrays.asList("src/test/java/ru/spbau/mit/file1.txt",
                "src/test/java/ru/spbau/mit/file2.txt");
        List<String> expected1 = Arrays.asList("In this example, widgets is a Collection<Widget>.");
        List<String> expected2 = Arrays.asList("In this example, widgets is a Collection<Widget>.",
                "We create a stream of Widget objects via Collection.stream(), filter it to produce",
                "Collections and streams, while bearing some superficial similarities, have different goals.",
                "Collections are primarily concerned with the efficient management of, and access to, their elements.");
        assertEquals(expected1, SecondPartTasks.findQuotes(fileNames, "In this example"));
        assertEquals(expected2, SecondPartTasks.findQuotes(fileNames, "Collection"));
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 0.1);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        final List<String> empty = Arrays.asList("");
        compositions.put("zero", empty);
        assertEquals("zero", SecondPartTasks.findPrinter(compositions));

        final List<String> first = Arrays.asList("a", "b", "c");
        final List<String> second = Arrays.asList("aaaa");
        compositions.put("one", first);
        compositions.put("two", second);
        assertEquals("two", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> a = new HashMap<>();
        a.put("apple", 100);
        Map<String, Integer> b = new HashMap<>();
        b.put("apple", 1);
        b.put("pear", 1);
        List<Map<String, Integer>> orders = Arrays.asList(a, b);

        Map<String, Integer> got = SecondPartTasks.calculateGlobalOrder(orders);
        assertEquals(2, got.size());
        assertEquals((Integer) 1, got.get("pear"));
        assertEquals((Integer) 101, got.get("apple"));
    }
}
