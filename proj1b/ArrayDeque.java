/**
 * Data structure that represents a Linked List Double Ended Queue (deque)
 * @author Daniel Vazquez Guevara
 * @param <T> type of item
 */
public class ArrayDeque<T> implements Deque<T> {
    // instance variables
    private T[] items;
    private int size;
    private int front;
    private int rear;
    private final int R_FACTOR = 2; // resizing factor
    private final double U_RATIO = 0.25; // minimum usage ratio

    /**
     * Creates an empty array deque.
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 0;
        rear = 0;
    }

    /**
     * Creates a deep copy of provided deque.
     * @param other the provided deque
     */
    public ArrayDeque(ArrayDeque other) {
        items = (T[]) new Object[other.items.length];
        size = other.size;
        front = other.front;
        rear = other.rear;
        System.arraycopy(other.items, 0, items, 0, other.items.length);
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item element to be added
     */
    @Override
    public void addFirst(T item) {
        resizeArrayAdd(); // makes sure array has enough space

        /* If the beginning of the array is reached, insert item at the end.
         * Otherwise, insert item at location front - 1
         */
        front = (front == 0) ? items.length - 1 : front - 1;
        items[front] = item;
        size += 1;
        // the first item is front and rear
        if (size == 1) {
            rear = front;
        }
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item element to be added
     */
    @Override
    public void addLast(T item) {
        resizeArrayAdd(); // makes sure there is space to add item into array
        /* If the end of the array is reached, insert item at the beginning.
         * Otherwise, insert item at location front + 1
         */

        rear = (rear == items.length - 1) ? 0 : rear + 1;
        items[rear] = item;
        size += 1;

        // the first item is front and rear
        if (size == 1) {
            front = rear;
        }
    }

    /**
     * Resizes the array by re-assigning it to a bigger one.
     */
    private void resizeArrayAdd() {
        if (size == items.length) {
            int currentIndex = front;
            T[] biggerArray = (T[]) new Object[R_FACTOR * items.length];
            for (int i = 0; i < size; i++) {
                biggerArray[i] = items[currentIndex];
                currentIndex = (currentIndex + 1) % items.length; // loop around array
            }
            items = biggerArray;
            front = 0;
            rear = size - 1;
        }
    }

    /**
     * Returns true if deque is empty, false otherwise.
     * @return result
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     * @return size of deque
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from front to rear, separated by a space.
     * Once all the items have been printed, print out a new line.
     *
     */
    @Override
    public void printDeque() {
        int currentIndex = front;
        for (int i = 0; i < size; i++) {
            System.out.print(items[currentIndex] + " ");
            if (currentIndex == items.length - 1) {
                currentIndex = 0;
            } else {
                currentIndex = currentIndex + 1;
            }
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return the removed item
     */
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T retrievedItem = items[front];
        items[front] = null; // remove reference to stored object
        front = (front == items.length - 1) ? 0 : front + 1;
        size -= 1;
        resizeArrayDel(); // makes sure the array is not too big
        return retrievedItem;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return the removed item
     */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T retrievedItem = items[rear];
        items[rear] = null; // remove reference to stored object
        rear = (rear == 0) ? items.length - 1 : rear - 1;
        size -= 1;
        resizeArrayDel(); // makes sure the array is not too big
        return retrievedItem;
    }

    /**
     * Resizes the array by re-assigning it to a bigger one.
     */
    private void resizeArrayDel() {
        double usageRatio = (double) size / items.length;
        if (items.length >= 16 && usageRatio < U_RATIO) {
            int currentIndex = front;
            T[] smallerArray = (T[]) new Object[items.length / 2];
            for (int i = 0; i < size; i++) {
                smallerArray[i] = items[currentIndex];
                currentIndex = (currentIndex + 1) % items.length; // loop around array
            }
            items = smallerArray;
            front = 0;
            rear = size - 1;
        }
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. It does not alter the deque!
     * Note: This method runs in constant time
     * @param index location of element to be retrieved
     * @return the item
     */
    @Override
    public T get(int index) {
        if (isEmpty() || (index < 0) || (index >= size)) {
            return null;
        }
        return items[(front + index) % items.length];
    }
}

