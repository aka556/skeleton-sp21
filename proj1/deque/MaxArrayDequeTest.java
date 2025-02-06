package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    /** define a comparator to some type that wo want test. */
    private static class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            }
            return o1.compareTo(o2);
        }
    }

    @Test
    public void maxWithoutComparatorTest() {
        MaxArrayDeque<Integer> mad1 = new MaxArrayDeque<>(new IntComparator());

        mad1.addLast(50);
        mad1.addLast(60);
        mad1.addFirst(90);
        
        Integer maxElement = mad1.max();

        assertEquals("max element should be 90", Integer.valueOf(90), maxElement);
    }

    @Test
    public void maxWithComparatorTest() {
        MaxArrayDeque<Integer> mad2 = new MaxArrayDeque<>(new IntComparator());

        mad2.addLast(10);
        mad2.addLast(16);
        mad2.addLast(45);
        mad2.addLast(9);

        Integer maxElement = mad2.max((a, b) -> b.compareTo(a));

        assertEquals("max element should be 9",Integer.valueOf(9), maxElement);
    }
}
