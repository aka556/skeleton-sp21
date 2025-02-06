package deque;

public interface Deque<Item> {
    void addFirst(Item item);

    void addLast(Item item);

    Item removeFirst();

    Item removeLast();

    Item get(int index);;

    int size();

    void printDeque();

    boolean equals(Object o);

    default boolean isEmpty() {
        return size() == 0;
    }
}
