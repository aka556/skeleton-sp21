package deque;

public interface Deque<T> {
    void addFirst(T item);

    void addLast(T item);

    T removeFirst();

    T removeLast();

    T get(int index);;

    int size();

    void printDeque();

    boolean equals(Object o);

    default boolean isEmpty() {
        return size() == 0;
    }
}
