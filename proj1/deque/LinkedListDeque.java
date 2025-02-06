package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class IntNode {
        private T item;
        private IntNode next;
        private IntNode prev;

        /** Construct function, init the linked construction. */
        public IntNode(T i, IntNode p, IntNode n) {
            item = i;
            prev = p;
            next = n;
        }

        @Override
        public String toString() {
            if (item == null) {
                return "null";
            }
            return item.toString();
        }
    }

    /** Linked Construction. */
    private IntNode sentinel;
    private IntNode head; // used for point the headNode
    private int size;

    /** Creates an empty linked list deque. */
    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null); // create a sentinel node
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        head = sentinel;
        size = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T x) {
        IntNode newNode = new IntNode(x, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;

        if (size == 0) {
            head = newNode;
        }
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T x) {
        IntNode newNode = new IntNode(x, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;

        if (size == 0) {
            head = newNode;
        }
        size += 1;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Removes and returns the item at the front of the deque. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T value = sentinel.next.item;

        if (size == 1) {
            head = sentinel;
            sentinel.next = sentinel.prev = sentinel;
        } else {
            head = head.next;
            sentinel.next = head;
            head.prev = sentinel;
        }

        size -= 1;
        return value;
    }

    /** Removes and returns the item at the back of the deque. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T value = sentinel.prev.item;

        if (size == 1) {
            head = sentinel;
            sentinel.next = sentinel.prev = sentinel;
        }
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        size -= 1;
        return value;
    }


    /**  Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        IntNode current = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }

    /** Gets the item at the given index. */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        IntNode current = sentinel.next;
        for (int i = 0; i < index; i += 1) {
            current = current.next;
        }
        return current.item;
    }

    /** Same as get, but uses recursion. */
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        IntNode current = sentinel.next;
        return getRecursiveHelper(current, index);
    }

    /** The helper method for recursive. */
    private T getRecursiveHelper(IntNode current, int index) {
        if (index == 0) {
            return current.item;
        }
        return getRecursiveHelper(current.next, index - 1);
    }

    /** The Deque objects we’ll make are iterable,
     *  so we must provide this method to return an iterator.
     * */
    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    /** DequeIterator implements the Iterator<Item> interface */
    private class DequeIterator implements Iterator<T> {
        private IntNode current = sentinel.next;

        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    /** Returns whether or not the parameter o is equal to the Deque. */
    public boolean equals(Object o) {
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<?> other = (LinkedListDeque<?>) o;
        if (this.size != other.size) {
            return false;
        }
        IntNode current = this.head.next;
        IntNode otherCurrent = (IntNode) other.head.next;
        while (current != sentinel) {
            if (!current.item.equals(otherCurrent.item)) {
                return false;
            }
            current = current.next;
            otherCurrent = otherCurrent.next;
        }
        return true;
    }

}
