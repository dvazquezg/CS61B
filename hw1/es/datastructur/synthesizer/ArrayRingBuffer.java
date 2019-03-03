package es.datastructur.synthesizer;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a bounded (fixed size) queue
 * @param <T> the type of items stored in the list
 */
public class ArrayRingBuffer<T>  implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // first, last, and fillCount should all be set to 0.
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (fillCount == rb.length) {
            throw new RuntimeException("Ring buffer overflow");
        }

        rb[last] = x;
        last = nextIndex(last);
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T item = rb[first];
        rb[first] = null; // remove object reference
        first = nextIndex(first);
        fillCount -= 1;
        return item;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T item = rb[first];
        return item;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    /**
     * Returns the correct index value wrapping around the array if necessary.
     * @param index
     * @return the correct circular index
     */
    private int nextIndex(int index) {
        return (index == rb.length - 1) ? 0 : index + 1;
    }

    /**
     * Returns true if the passed ArrayRing contains the same values as this
     * @return
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) { // good optimization
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        ArrayRingBuffer other = (ArrayRingBuffer<T>) o;
        if (this.fillCount() != other.fillCount()) {
            return false;
        }
        if (!this.peek().equals(other.peek())) { // cheap optimization
            return false;
        }
        // iterate over each element in parallel
        Iterator<T> it1 = this.iterator();
        Iterator<T> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!it1.next().equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a new iterator
     * @return iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayRingIterator();
    }

    /***
     * Private class that supports iteration
     * @author Daniel Vazquez Guevara
     */
    private class ArrayRingIterator implements Iterator<T> {
        private int currentIndex;
        private int count;
        ArrayRingIterator() {
            currentIndex = first;
            count = 0 ;
        }

        /**
         * Checks if there exist more items to iterate over
         * @return boolean value indicating
         */
        public boolean hasNext() {
            return count < fillCount; // last points to next available slot
        }

        /**
         * Return the next item if available, throws an exception otherwise
         * @return the item
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to iterate over.");
            }
            currentIndex = nextIndex(currentIndex);
            count++;
            return rb[currentIndex];
        }
    }
}
