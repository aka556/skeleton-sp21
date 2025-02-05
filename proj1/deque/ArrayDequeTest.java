package deque;

import edu.princeton.cs.algs4.StdRandom;
import net.sf.saxon.ma.arrays.ArrayFunctionSet;
import org.junit.Test;
import java.lang.reflect.Array;
import java.util.Random;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addIsEmptyTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());

        ad1.addFirst("CS61A");
        assertEquals("Should be size 1", 1, ad1.size());
        assertFalse("Should not be empty", ad1.isEmpty());

        ad1.addLast("CS61B");
        assertEquals("Should be size 2", 2, ad1.size());

        ad1.addLast("CS61C");
        assertEquals("Should be size 3", 3, ad1.size());

        System.out.println("Printing out Deque: ");
        ad1.printDeque();
    }

    @Test
    /** test remove method. */
    public void addRemoveTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());

        ad1.addLast(1);
        assertFalse("Should not be empty", ad1.isEmpty());

        ad1.removeFirst();
        assertTrue("Should be empty", ad1.isEmpty());

        assertNull("Should be null when there are no items", ad1.removeFirst());
    }

    @Test
    public void removeEmptyTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());

        ad1.addLast(16);

        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = ad1.size();

        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /** test multiple params whether useful. */
    public void multipleParamsTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ArrayDeque<String> ad2 = new ArrayDeque<>();
        ArrayDeque<Double> ad3 = new ArrayDeque<>();
        ArrayDeque<Boolean> ad4 = new ArrayDeque<>();

        ad1.addLast(1);
        ad2.addLast("CS106B");
        ad3.addLast(3.14159);
        ad4.addLast(true);

        int a = ad1.removeFirst();
        String b = ad2.removeFirst();
        Double c = ad3.removeFirst();
        Boolean d = ad4.removeFirst();

        assertTrue("Should be empty", ad1.isEmpty());
        assertTrue("Should be empty", ad2.isEmpty());
        assertTrue("Should be empty", ad3.isEmpty());
        assertTrue("Should be empty", ad4.isEmpty());
    }

    @Test
    public void emptyNullReturnTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        assertEquals("Should return true when removeFirst is called on an empty Deque", null, ad1.removeFirst());
        assertEquals("Should return true when removeFirst is called on an empty Deque", null, ad1.removeFirst());
        assertEquals("Should return true when removeFirst is called on an empty Deque", null, ad1.removeLast());
    }

    @Test
    public void bigADequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        for (int i = 0; i < 10; i++) {
            ad1.addLast(i);
        }

        for (int i = 0; i < 5; i++) {
            assertEquals("Should have same value", i, (double) ad1.removeFirst(), 0.0);
        }

        for (int i = 9; i >= 5; i--) {
            assertEquals("Should have same value", i, (double) ad1.removeLast(), 0.0);
        }

        assertTrue("Should be empty", ad1.isEmpty());
    }

    @Test
    /** Test the get method. */
    public void getTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        for (int i = 0; i < 1000; i++) {
            ad1.addLast(i);
        }

        for (int i = 0; i < 1000; i++) {
            assertEquals("Should have same value", i, (int) ad1.get(i));
        }

        assertNull("Should be null when index out of bound", ad1.get(1000));
        assertNull("Should be null when index out of bound", ad1.get(-5));
    }

    @Test
    /** Test the iterator method. */
    public void iteratorTest() {
        ArrayDeque<Integer> myList = new ArrayDeque<>();

        for (int i = 0; i < 1000; i++) {
            myList.addLast(i);
        }

        int index = 0;
        for (int i = 0; i < 1000; i++) {
            assertEquals("Should have same value", i, index);
            index += 1;
        }
    }

    @Test
    /** Test the equals' method. */
    public void equalsTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();

        ad1.addLast(1);
        ad2.addLast(1);
        assertEquals(ad1, ad2);

        ad1.addLast(2);
        ad2.addLast(2);
        assertEquals(ad1, ad2);

        ad1.addLast(4);
        ad1.addLast(5);
        assertNotEquals(ad1, ad2);

        ad1.removeFirst();
        assertNotEquals(ad1, ad2);
    }

    @Test
    /** Randomized test. */
    public void randomizedTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();

        int n = 100000;
        for (int i = 0; i < n; i++) {
            int operationNumbers = StdRandom.uniform(0, 5);;
            if (operationNumbers == 0) {
                int randVal = StdRandom.uniform(0, 100);
                ad1.addFirst(randVal);
                ad2.addFirst(randVal);
                assertEquals(ad1, ad2);
            } else if (operationNumbers == 1) {
                int randVal = StdRandom.uniform(0, 100);
                ad1.addLast(randVal);
                ad2.addLast(randVal);
                assertEquals(ad1, ad2);
            } else if (ad1.size() == 0) {
                assertTrue(ad1.isEmpty());
                assertTrue(ad2.isEmpty());
            } else if (operationNumbers == 2 && !ad1.isEmpty()) {
                assertEquals(ad1.removeFirst(), ad2.removeFirst());
            } else if (operationNumbers == 3 && !ad1.isEmpty()) {
                assertEquals(ad1.removeLast(), ad2.removeLast());
            } else if (operationNumbers == 4) {
                int randIndex = StdRandom.uniform(0, ad1.size());
                ad1.get(randIndex);
                ad2.get(randIndex);
            }
        }
    }
}
