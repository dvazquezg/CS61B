package es.datastructur.synthesizer;

/**
 * Interface for a basic implementation of a Bounded Queue
 * @author Daniel Vazquez Guevara
 */
public interface BoundedQueue<T> extends Iterable<T> {
    /** return size of the buffer */
    int capacity();

    /** return number of items currently in the buffer */
    int fillCount();

    /** add item x to the end */
    void enqueue(T x);

    /** delete and return item from the front */
    T dequeue();

    /** return (but do not delete) item from the front */
    T peek();

    /** return true if the list is empty, false otherwise */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /** return true if queue is full, false otherwise*/
    default boolean isFull() {
        return fillCount() == capacity();
    }

}
