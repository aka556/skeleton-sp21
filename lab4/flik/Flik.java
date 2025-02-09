package flik;

import edu.princeton.cs.algs4.StdRandom;
import static org.junit.Assert.assertTrue;

/** An Integer tester created by Flik Enterprises.
 * @author Josh Hug
 * */
public class Flik {
    /** @param a Value 1
     *  @param b Value 2
     *  @return Whether a and b are the same */
    public static boolean isSameNumber(Integer a, Integer b) {
        /** Integer is Wrapper Class, it's not only compare with numbers,
         *  but also compare with types.
         *  for example, Integer a = 128, Integer b = 128,
         *  they are equal, because they type are not same.
         *  There can be two methods, one is use int type,
         *  another is use a.equals(b)
         */
        return a.equals(b);
    }

}
