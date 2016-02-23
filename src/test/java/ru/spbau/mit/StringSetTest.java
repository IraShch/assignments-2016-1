package ru.spbau.mit;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StringSetTest {
    @Test
    public void testSimple() {
        StringSet stringSet = instance();
        assertEquals(0, stringSet.size());
        assertEquals(0, stringSet.howManyStartsWithPrefix("AAAAAAAAAAA"));

        stringSet.add("");
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix(""));
        assertEquals(0, stringSet.howManyStartsWithPrefix("a"));
        assertEquals(true, stringSet.add("AAA"));
        assertEquals(true, stringSet.add("A"));
        assertEquals(true, stringSet.add("AAAAA"));
        assertEquals(false, stringSet.add("AAA"));
        stringSet.remove("AAAAA");
        assertEquals(2, stringSet.howManyStartsWithPrefix("A"));
        assertEquals(false, stringSet.contains("AAAAA"));
        stringSet.remove("A");
        stringSet.remove("AAA");
        assertEquals(1, stringSet.size());
        stringSet.remove("");
        assertEquals(0, stringSet.size());
        assertEquals(false, stringSet.contains("A"));
        assertEquals(false, stringSet.contains(""));

//        assertTrue(stringSet.add("abc"));
//        assertTrue(stringSet.contains("abc"));
//        assertEquals(1, stringSet.size());
//        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
//
//        assertTrue(stringSet.add("banana"));
//        assertTrue(stringSet.add("bana"));
//        assertTrue(stringSet.contains("banana"));
//        assertTrue(stringSet.contains("bana"));
//        assertTrue(!stringSet.contains("ban"));
//        assertTrue(!stringSet.contains("c"));
//        assertEquals(2, stringSet.howManyStartsWithPrefix("ba"));
//        assertEquals(0, stringSet.howManyStartsWithPrefix("c"));
//        assertEquals(2, stringSet.howManyStartsWithPrefix("bana"));
//        assertEquals(3, stringSet.howManyStartsWithPrefix(""));
//        assertTrue(stringSet.remove("bana"));
//        assertFalse(stringSet.contains("bana"));
//        assertEquals(1, stringSet.howManyStartsWithPrefix("bana"));
//        assertEquals(1, stringSet.howManyStartsWithPrefix("b"));
//        assertEquals(2, stringSet.howManyStartsWithPrefix(""));
//        assertTrue(stringSet.contains("banana"));

    }

    public static StringSet instance() {
        try {
            return (StringSet) Class.forName("ru.spbau.mit.StringSetImpl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Error while class loading");
    }
}
