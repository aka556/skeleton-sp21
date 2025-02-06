package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private final Comparator<Item> comparator;

    /** creates a MaxArrayDeque with the given Comparator. */
    public MaxArrayDeque(Comparator<Item> c) {
        comparator = c;
    }

    /** returns the maximum element in the deque as governed by the previously given Comparator. */
    public Item max() {
        return max(comparator);
    }

    /** returns the maximum element in the deque as governed by the parameter Comparator c. */
    public Item max(Comparator<Item> c) {
        if (this.isEmpty()) {
            return null;
        }

        Item maxItem = items[front];
        for (int i = 1; i < size(); i++) {
            int currentIndex = (front + i) % items.length;
            if (c.compare(items[currentIndex], maxItem) > 0) {
                maxItem = items[currentIndex];
            }
        }
        return maxItem;
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
