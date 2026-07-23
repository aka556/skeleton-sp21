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

        StringBuilder operations = new StringBuilder();
        int N = 5000;

        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addLast(randVal);
                solutionDeque.addLast(randVal);
                operations.append("addLast(").append(randVal).append(")\n");
            } else if (operationNumber == 1) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addFirst(randVal);
                solutionDeque.addFirst(randVal);
                operations.append("addFirst(").append(randVal).append(")\n");
            } else if (operationNumber == 2) {
                int studentSize = studentDeque.size();
                int solutionSize = solutionDeque.size();
                if (studentSize != solutionSize) {
                    operations.append("size()\n");
                    System.out.println(operations.toString());
                    assertEquals(operations.toString(), solutionSize, studentSize);
                }
            } else if (operationNumber == 3) {
                boolean studentEmpty = studentDeque.isEmpty();
                boolean solutionEmpty = solutionDeque.isEmpty();
                if (studentEmpty != solutionEmpty) {
                    operations.append("isEmpty()\n");
                    System.out.println(operations.toString());
                    assertEquals(operations.toString(), solutionEmpty, studentEmpty);
                }
            }
        }
    }

    @Test
    public void testRemoveFirst() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        StringBuilder operations = new StringBuilder();

        // Add 50 elements
        for (int i = 0; i < 50; i++) {
            studentDeque.addLast(i);
            solutionDeque.addLast(i);
            operations.append("addLast(").append(i).append(")\n");
        }

        // Remove and compare
        for (int i = 0; i < 50; i++) {
            Integer expected = solutionDeque.removeFirst();
            Integer actual = studentDeque.removeFirst();
            operations.append("removeFirst()\n");

            if (!expected.equals(actual)) {
                System.out.println(operations.toString());
                assertEquals(operations.toString(), expected, actual);
            }
        }
    }

    @Test
    public void testRemoveLast() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        StringBuilder operations = new StringBuilder();

        // Add 50 elements
        for (int i = 0; i < 50; i++) {
            studentDeque.addLast(i);
            solutionDeque.addLast(i);
            operations.append("addLast(").append(i).append(")\n");
        }

        // Remove and compare
        for (int i = 0; i < 50; i++) {
            Integer expected = solutionDeque.removeLast();
            Integer actual = studentDeque.removeLast();
            operations.append("removeLast()\n");

            if (!expected.equals(actual)) {
                System.out.println(operations.toString());
                assertEquals(operations.toString(), expected, actual);
            }
        }
    }

    @Test
    public void testMixedOperations() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        StringBuilder operations = new StringBuilder();

        for (int i = 0; i < 200; i++) {
            int operationNumber = StdRandom.uniform(0, 5);

            if (operationNumber == 0) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addFirst(randVal);
                solutionDeque.addFirst(randVal);
                operations.append("addFirst(").append(randVal).append(")\n");
            } else if (operationNumber == 1) {
                Integer randVal = StdRandom.uniform(0, 100);
                studentDeque.addLast(randVal);
                solutionDeque.addLast(randVal);
                operations.append("addLast(").append(randVal).append(")\n");
            } else if (operationNumber == 2 && !solutionDeque.isEmpty()) {
                Integer expected = solutionDeque.removeFirst();
                Integer actual = studentDeque.removeFirst();
                operations.append("removeFirst()\n");

                if (!expected.equals(actual)) {
                    System.out.println(operations.toString());
                    assertEquals(operations.toString(), expected, actual);
                }
            } else if (operationNumber == 3 && !solutionDeque.isEmpty()) {
                Integer expected = solutionDeque.removeLast();
                Integer actual = studentDeque.removeLast();
                operations.append("removeLast()\n");

                if (!expected.equals(actual)) {
                    System.out.println(operations.toString());
                    assertEquals(operations.toString(), expected, actual);
                }
            } else if (operationNumber == 4 && !solutionDeque.isEmpty()) {
                int index = StdRandom.uniform(0, solutionDeque.size());
                Integer expected = solutionDeque.get(index);
                Integer actual = studentDeque.get(index);
                operations.append("get(").append(index).append(")\n");

                if (!expected.equals(actual)) {
                    System.out.println(operations.toString());
                    assertEquals(operations.toString(), expected, actual);
                }
            }
        }
    }
}
