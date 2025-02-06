package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();
        LinkedListDeque<Integer> lld4 = new LinkedListDeque<Integer>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);
        lld4.addFirst(5);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
        int a = lld4.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        boolean passed3 = false;
        assertEquals("Should return true when removeFirst is called on an empty Deque", null, lld1.removeFirst());
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /** test the . */
    public void nonEmptyInstantiationTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        lld1.addFirst(5);

        assertFalse("Should not be empty", lld1.isEmpty());
        assertEquals("Should be size 1", 1, lld1.size());
    }

    @Test
    /** Test the iterable get method. */
    public void getTest() {
        LinkedListDeque<Integer> myList = new LinkedListDeque<Integer>();

        for (int i = 0; i < 100; i++) {
            myList.addLast(i);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals("Should have the same value", i, (int) myList.get(i));
        }

        assertNull("Should be null when index out of range", myList.get(100));
        assertNull("Should be null when index out of range", myList.get(-5));
    }

    @Test
    /** Test the recursive method. */
    public void getRecursiveTest() {
        LinkedListDeque<Integer> myList = new LinkedListDeque<>();
        int default_size = 1000;
        for (int i = 0; i < default_size; i++) {
            myList.addFirst(i);
        }

        for (int i = 0; i < default_size; i++) {
            assertEquals("Should have the same value", default_size - i - 1, (int) myList.getRecursive(i));
        }

        assertNull("Should be null when index out of range", myList.getRecursive(1000));
        assertNull("Should be null when index out of range", myList.getRecursive(-5));
    }

    @Test
    /** Test the iterator method. */
    public void iteratorTest() {
        LinkedListDeque<Integer> myList = new LinkedListDeque<Integer>();

        for (int i = 0; i < 100; i++) {
            myList.addLast(i);
        }

        int index = 0;
        for (int i = 0; i < 100; i++) {
            assertEquals("Should have the same value", i, index);
            index += 1;
        }
    }

    @Test
    /** Test the Equals method. */
    public void equalsTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();

        lld1.addLast(1);
        lld2.addLast(1);
        assertEquals(lld1, lld2);

        lld1.addLast(2);
        lld2.addLast(2);
        assertEquals(lld1, lld2);

        lld1.addLast(3);
        assertNotEquals(lld1, lld2);

        lld2.addLast(4);
        assertNotEquals(lld1, lld2);
    }

    @Test
    /** Randomized test for all LinKedListDeque. */
    public void randomizedTest() {
        LinkedListDeque<Integer> myList = new LinkedListDeque<>();

        int testSize = 5000;;
        for (int i = 0; i < testSize; i++) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                myList.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                myList.addLast(randVal);
            } else if (myList.size() == 0) {
                assertTrue(myList.isEmpty());
            } else if (operationNumber == 2) {
                assertTrue(myList.size() > 0);
            } else if (operationNumber == 3 && !myList.isEmpty()) {
                myList.removeFirst();
            } else if (operationNumber == 4 && !myList.isEmpty()) {
                myList.removeLast();
            } else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, myList.size());
                myList.get(randIndex);
                myList.getRecursive(randIndex);
            }
        }


    }
}
