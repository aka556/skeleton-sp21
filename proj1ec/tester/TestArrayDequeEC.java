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
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addLast(randVal);
                solutionDeque.addLast(randVal);
            } else if (operationNumber == 1) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addFirst(randVal);
                solutionDeque.addFirst(randVal);
            } else if (operationNumber == 2) {
                assertEquals("size()", solutionDeque.size(), studentDeque.size());
            } else if (operationNumber == 3) {
                assertEquals("isEmpty()", solutionDeque.isEmpty(), studentDeque.isEmpty());
            }
        }
    }

    @Test
    public void testRemoveFirst() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        // Add 50 elements
        for (int i = 0; i < 50; i++) {
            studentDeque.addLast(i);
            solutionDeque.addLast(i);
        }

        // Remove and compare
        for (int i = 0; i < 50; i++) {
            Integer expected = solutionDeque.removeFirst();
            Integer actual = studentDeque.removeFirst();
            assertEquals("removeFirst()", expected, actual);
        }
    }

    @Test
    public void testRemoveLast() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        // Add 50 elements
        for (int i = 0; i < 50; i++) {
            studentDeque.addLast(i);
            solutionDeque.addLast(i);
        }

        // Remove and compare
        for (int i = 0; i < 50; i++) {
            Integer expected = solutionDeque.removeLast();
            Integer actual = studentDeque.removeLast();
            assertEquals("removeLast()", expected, actual);
        }
    }

    @Test
    public void testGet() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        // Add elements
        for (int i = 0; i < 50; i++) {
            if (StdRandom.bernoulli()) {
                studentDeque.addFirst(i);
                solutionDeque.addFirst(i);
            } else {
                studentDeque.addLast(i);
                solutionDeque.addLast(i);
            }
        }

        // Test get
        for (int i = 0; i < solutionDeque.size(); i++) {
            Integer expected = solutionDeque.get(i);
            Integer actual = studentDeque.get(i);
            assertEquals("get(" + i + ")", expected, actual);
        }
    }

    @Test
    public void testMixedOperations() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        for (int i = 0; i < 200; i++) {
            int operationNumber = StdRandom.uniform(0, 5);

            if (operationNumber == 0) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addFirst(randVal);
                solutionDeque.addFirst(randVal);
            } else if (operationNumber == 1) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addLast(randVal);
                solutionDeque.addLast(randVal);
            } else if (operationNumber == 2 && !solutionDeque.isEmpty()) {
                Integer expected = solutionDeque.removeFirst();
                Integer actual = studentDeque.removeFirst();
                assertEquals("removeFirst()", expected, actual);
            } else if (operationNumber == 3 && !solutionDeque.isEmpty()) {
                Integer expected = solutionDeque.removeLast();
                Integer actual = studentDeque.removeLast();
                assertEquals("removeLast()", expected, actual);
            } else if (operationNumber == 4 && !solutionDeque.isEmpty()) {
                int index = StdRandom.uniform(0, solutionDeque.size());
                Integer expected = solutionDeque.get(index);
                Integer actual = studentDeque.get(index);
                assertEquals("get(" + index + ")", expected, actual);
            }
        }
    }
}
