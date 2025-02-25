package deque;

/** 封装， 提供一个接口.
 * @param <T>
 * 实现oop的封装特性
 * 其中里面的方法是为调用接口的类提供的，
 * 可以进行方法重写override
 */
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
