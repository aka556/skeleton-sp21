package flik;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/** Test the Flik method. */
public class FlikTest {
    @Test
    public void randomizedTest() {
        for (int i = 0, j = 0; i < 500; i++, j++) {
            if (!Flik.isSameNumber(i, j)) {
                throw new AssertionError(
                        String.format("%d is not same %d", i, j)
                );
            }
        }
    }
}
