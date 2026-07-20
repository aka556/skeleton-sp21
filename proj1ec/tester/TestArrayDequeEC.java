package tester;

import org.junit.Test;
import static org.junit.Assert.*;

import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Randomized testing for StudentArrayDeque.
 * Compares StudentArrayDeque behavior with ArrayDequeSolution.
 */
public class TestArrayDequeEC {

    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);

            if (operationNumber == 0) {
                // addFirst
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addFirst(randVal);
                solutionDeque.addFirst(randVal);
                assertEquals("addFirst(" + randVal + ")",
                        solutionDeque.size(), studentDeque.size());
            } else if (operationNumber == 1) {
                // addLast
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addLast(randVal);
                solutionDeque.addLast(randVal);
                assertEquals("addLast(" + randVal + ")",
                        solutionDeque.size(), studentDeque.size());
            } else if (operationNumber == 2) {
                // size
                assertEquals("size()",
                        solutionDeque.size(), studentDeque.size());
            } else if (operationNumber == 3) {
                // removeFirst
                if (!solutionDeque.isEmpty()) {
                    Integer expected = solutionDeque.removeFirst();
                    Integer actual = studentDeque.removeFirst();
                    assertEquals("removeFirst()", expected, actual);
                }
            } else if (operationNumber == 4) {
                // removeLast
                if (!solutionDeque.isEmpty()) {
                    Integer expected = solutionDeque.removeLast();
                    Integer actual = studentDeque.removeLast();
                    assertEquals("removeLast()", expected, actual);
                }
            } else if (operationNumber == 5) {
                // get
                if (!solutionDeque.isEmpty()) {
                    int index = StdRandom.uniform(0, solutionDeque.size());
                    Integer expected = solutionDeque.get(index);
                    Integer actual = studentDeque.get(index);
                    assertEquals("get(" + index + ")", expected, actual);
                }
            }
        }
    }

    @Test
    public void testAddFirstAndRemoveFirst() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        for (int i = 0; i < 100; i++) {
            studentDeque.addFirst(i);
            solutionDeque.addFirst(i);
        }

        for (int i = 0; i < 100; i++) {
            Integer expected = solutionDeque.removeFirst();
            Integer actual = studentDeque.removeFirst();
            assertEquals("removeFirst() after addFirst", expected, actual);
        }
    }

    @Test
    public void testAddLastAndRemoveLast() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        for (int i = 0; i < 100; i++) {
            studentDeque.addLast(i);
            solutionDeque.addLast(i);
        }

        for (int i = 0; i < 100; i++) {
            Integer expected = solutionDeque.removeLast();
            Integer actual = studentDeque.removeLast();
            assertEquals("removeLast() after addLast", expected, actual);
        }
    }

    @Test
    public void testMixedOperations() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        // Mix of addFirst and addLast
        for (int i = 0; i < 50; i++) {
            studentDeque.addFirst(i);
            solutionDeque.addFirst(i);
            studentDeque.addLast(i + 50);
            solutionDeque.addLast(i + 50);
        }

        // Check size
        assertEquals("size after mixed adds", solutionDeque.size(), studentDeque.size());

        // Remove from both ends
        for (int i = 0; i < 25; i++) {
            Integer expectedFirst = solutionDeque.removeFirst();
            Integer actualFirst = studentDeque.removeFirst();
            assertEquals("removeFirst mixed", expectedFirst, actualFirst);

            Integer expectedLast = solutionDeque.removeLast();
            Integer actualLast = studentDeque.removeLast();
            assertEquals("removeLast mixed", expectedLast, actualLast);
        }
    }

    @Test
    public void testGetAfterOperations() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        for (int i = 0; i < 200; i++) {
            if (StdRandom.bernoulli()) {
                studentDeque.addFirst(i);
                solutionDeque.addFirst(i);
            } else {
                studentDeque.addLast(i);
                solutionDeque.addLast(i);
            }
        }

        // Test get for all valid indices
        for (int i = 0; i < solutionDeque.size(); i++) {
            Integer expected = solutionDeque.get(i);
            Integer actual = studentDeque.get(i);
            assertEquals("get(" + i + ")", expected, actual);
        }
    }

    @Test
    public void testEmptyDeque() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();

        assertTrue("isEmpty on new deque", studentDeque.isEmpty());
        assertEquals("size on empty deque", 0, studentDeque.size());
        assertNull("removeFirst on empty", studentDeque.removeFirst());
        assertNull("removeLast on empty", studentDeque.removeLast());
    }
}
