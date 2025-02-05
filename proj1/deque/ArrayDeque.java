package deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private static final int DEFAULT_CAPACITY = 8;
    private static final double RESIZE_FACTOR = 2.0;

    /** The pointer that point the first and end element. */
    private int front;
    private int rear;

    /** Creates an empty array deque. */
    public ArrayDeque() {
        items = (Item[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        front = 0;
        rear = 0;
    }

    /** Resize the size of the items. */
    public void resize(int capicity) {
        capicity = Math.max(capicity, DEFAULT_CAPACITY);
        Item[] newItems = (Item[]) new Object[capicity];
        System.arraycopy(items, 0, newItems, 0, size);
        items = newItems;

        front = 0;
        rear = size;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(Item item) {
        if (size == items.length) {
            resize((int) Math.round(items.length * RESIZE_FACTOR));
        }

        front = (front - 1 + items.length) % items.length;
        items[front] = item;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(Item item) {
        if (size == items.length) {
            resize((int) Math.round(items.length * RESIZE_FACTOR));
        }

        items[rear] = item;
        rear = (rear + 1) % items.length;
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Removes and returns the item at the front of the deque. */
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }

        Item removeItem = items[front];
        items[front] = null;
        front = (front + 1) % items.length;
        size -= 1;

        shrinkSize();
        return removeItem;
    }

    /** Removes and returns the item at the back of the deque. */
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }

        rear = (rear - 1 + items.length) % items.length;
        Item removeItem = items[rear];
        items[rear] = null;
        size -= 1;

        shrinkSize();
        return removeItem;
    }

    /** When removed the items, adjust the size of items. */
    public void shrinkSize() {
        if (isEmpty()) {
            resize(DEFAULT_CAPACITY);
        } else if (items.length / 4 > size && size > 4) {
            resize(size * 2);
        }
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        if (size == 0) {
            return;
        }

        int currentIndex = front;
        for (int i = 0; i < size; i++) {
            System.out.print(items[currentIndex] + " ");
            currentIndex = (currentIndex + 1) % items.length;
        }
        System.out.println();
    }

    /** Gets the item at the given index. */
    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[index];
    }

    /** he Deque objects we’ll make are iterable, so we must provide this method to return an iterator. */
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int current = front;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item item = items[current];
                current = (current + 1) % items.length;
                count += 1;
                return item;
            }
        };
    }

    /** Returns whether or not the parameter o is equal to the Deque. */
    public boolean equals(Object o) {
        if (!(o instanceof ArrayDeque)) {
            return false;
        }

        ArrayDeque other = (ArrayDeque) o;
        if (size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            int thisIndex = (front + i) % items.length;
            int otherIndex = (other.front + i) % other.items.length;

            if (items[thisIndex] != other.items[otherIndex]) {
                return false;
            }
        }
        return true;
    }

}
