package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;

    /** creates a MaxArrayDeque with the given Comparator. */
    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    /** returns the maximum element in the deque as governed by the previously given Comparator. */
    public T max() {
        return max(comparator);
    }

    /** returns the maximum element in the deque as governed by the parameter Comparator c. */
    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }

//        T maxItem = items[front];
//        for (int i = 1; i < size(); i++) {
//            int currentIndex = (front + i) % items.length;
//            if (c.compare(items[currentIndex], maxItem) > 0) {
//                maxItem = items[currentIndex];
//            }
//        }
//        return maxItem;
        int maxIndex = 0;
        for (int i = 1; i < size(); i++) {
            if (comparator.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }

    @Override
    /** eturns whether or not the parameter o is equal to the Deque. */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof MaxArrayDeque)) {
            return false;
        }

        MaxArrayDeque<?> other = (MaxArrayDeque<?>) o;

        if (other.max() != max()) {
            return false;
        }

        return super.equals(o);
    }
}
