package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    /** @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> buggy =  new BuggyAList<>();
        AListNoResizing <Integer> normal = new AListNoResizing<>();

        buggy.addLast(4);
        normal.addLast(4);

        buggy.addLast(5);
        normal.addLast(5);

        buggy.addLast(6);
        normal.addLast(6);

        assertEquals(buggy.size(), normal.size());

        assertEquals(buggy.removeLast(), normal.removeLast());
        assertEquals(buggy.removeLast(), normal.removeLast());
        assertEquals(buggy.removeLast(), normal.removeLast());
    } */

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> H = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                H.addLast(randVal);
            }
            else if (operationNumber == 1 && L.size() > 0) {
                // getLast
                int last = L.getLast();
            }
            else if (operationNumber == 2 && L.size() > 0 && H.size() > 0) {
                // removeLast
                assertEquals(L.removeLast(), H.removeLast());
            }
        }
    }
}
