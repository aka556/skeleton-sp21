package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed); // will be true
    }

    @Test
    public void testSquarePrimesSimple1() {
        IntList lst = IntList.of(3, 12, 2, 16, 19, 20);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 12 -> 4 -> 16 -> 361 -> 20", lst.toString());
        assertTrue(changed); // will be true
    }

    @Test
    public void testSquarePrimesSimple2() {
        IntList lst = IntList.of(4, 12, 18, 24, 25);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 12 -> 18 -> 24 -> 25", lst.toString());
        assertFalse(changed); // will be false
    }

    @Test
    public void testSquarePrimesSimple3() {
        IntList lst = IntList.of(4, 6, 9, 10, 16);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 6 -> 9 -> 10 -> 16", lst.toString());
        assertFalse(changed); // will be false
    }

    @Test
    public void testSquarePrimesSimple4() {
        IntList lst = IntList.of(11, 13, 17, 19, 23);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("121 -> 169 -> 289 -> 361 -> 529", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple5(){
        IntList lst = IntList.of(2, 3, 5, 7, 11, 13, 17);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 9 -> 25 -> 49 -> 121 -> 169 -> 289", lst.toString());
        assertTrue(changed);
    }
}
