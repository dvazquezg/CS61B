/**
 * Data structure that represents a Linked List Double Ended Queue (deque)
 * @author Daniel Vazquez Guevara
 * @param <T> type of item
 */
public class LinkedListDeque<T> {
    /**
     * Represent a Node of a deque
     */
    private class Node {
        private T item;
        private Node next;
        private Node prev;
        private Node(T i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    // instance variables
    private Node sentinel;
    private int size;

    /**
     * Creates an empty linked list deque.
     */
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        // set prev and next pointing to sentinel (itself)
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /**
     * Creates a deep copy of provided deque.
     * @param other the provided deque
     */
    public LinkedListDeque(LinkedListDeque other) {
        this(); // call default non-parameter constructor
        // deep copy loop
        Node p = other.sentinel.next; // helper pointer
        while (p != other.sentinel) {
            addFirst(p.item);
            p = p.next;
        }
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item element to be added
     */
    public void addFirst(T item) {
        sentinel.next = new Node(item, sentinel.next, sentinel);
        // linking the older sentinel.next.prev to the newly created sentinel.next
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item element to be added
     */
    public void addLast(T item) {
        /*
        sentinel.prev.next = new Node(item, sentinel, sentinel.prev);
        // linking the older sentinel.next.prev to the newly created sentinel.next
        sentinel.prev = sentinel.prev.next;
        size += 1;*/
        sentinel.prev = new Node(item, sentinel, sentinel.prev);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     * @return result
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     * @return size of deque
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     *
     */
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    /**
     * Prints the items in the deque from last to first, separated by a space.
     * Once all the items have been printed, print out a new line.
     *
     */
    public void printDequeBackwards() {
        Node p = sentinel.prev;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.prev;
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return the removed item
     */
    public T removeFirst() {
        return null;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return the removed item
     */
    public T removeLast() {
        return null;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. It does not alter the deque!
     * Note: This method uses iteration
     * @param index location of element to be retrieved
     * @return the item
     */
    public T get(int index) {
        return null;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. It does not alter the deque!
     * Note: This method uses recursion
     * @param index location of element to be retrieved
     * @return the item
     */
    public T getRecursive(int index) {
        return null;
    }
}
