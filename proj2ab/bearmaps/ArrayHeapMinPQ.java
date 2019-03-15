package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * An efficient implementation of the ExtrinsicMinPQ.
 * It implements an ArrayList as the core data structure
 * Note that size, get and set operations of ArrayList runs in
 * constant time. Add and remove operations runs at amortized constant time.
 * @author Daniel Vazquez Guevara
 * @version 1.0
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode<T>> heap;
    private HashMap<T, Integer> map; // map to store item an index
    private final int root = 1; // we offset insertion by 1
    private int size; // number of items in heap

    /**
     * Creates an instance of ArrayHeapPQ
     */
    public ArrayHeapMinPQ() {
        heap = new ArrayList<>();
        map = new HashMap<>();
        // skip 1st pos (it allow us to simplify index operations)
        heap.add(new PriorityNode<>(null, -1));
        size = 0;
    }

    /**
     * Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * You may assume that item is never null.
     * Runs in O(log(n)) since swimUp operation may happen
     * @param item the item to be inserted
     * @param priority the priority of the item
     */
    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException("Item is" + (item == null ? "null" : " in Heap"));
        }
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);
        size += 1; // increase size
        map.put(item, size); // add item to map
        heap.add(newNode); // add new node to end of ArrayList
        swimUp(size); // relocate new node (at location size) to its appropriate location
    }

    /**
     * Relocate Node at given index to the appropriate upper
     * location if needed
     * Runs in O(log(n))
     * @param index the location to be relocated
     */
    private void swimUp(int index) {
        if (heap.get(parent(index)).getPriority() > heap.get(index).getPriority()) {
            swap(index, parent(index));
            swimUp(parent(index)); // recursive call to continue relocating node if necessary
        }
    }

    /**
     * Swap two node at the given locations
     * Runs in O(1)
     * @param index1 first node
     * @param index2 second node
     */
    private void swap(int index1, int index2) {
        PriorityNode<T> temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }

    /**
     * Returns true if the PQ contains the given item.
     * Runs in O(1)* (assuming the hash function disperses
     * the elements properly among the buckets)
     * @param item
     * @return
     */
    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        return map.get(item) != null;
    }

    /**
     * Returns the minimum item (the one located a the root).
     * Throws NoSuchElementException if the PQ is empty.
     * Runs in O(1)
     * @return the smallest item without removing it
     */
    @Override
    public T getSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("Empty heap");
        }
        return heap.get(1).getItem();
    }


    /**
     * Removes and returns the minimum item.
     * Throws NoSuchElementException if the PQ is empty.
     * Runs in O(log(n)) since swimDown operation may happen
     * @return the smallest item after removing it
     */
    @Override
    public T removeSmallest() {
        if (size == 0) {
            throw  new NoSuchElementException("Empty heap");
        }
        T smallest = heap.get(root).getItem();
        swap(root, size);
        map.remove(heap.get(size).getItem()); // remove item from map
        heap.remove(size); // remove last node from heap
        size -= 1; // decrease size
        swimDown(root);
        return smallest;
    }

    /**
     * Relocate Node at given index to the appropriate lower
     * location if needed
     * Runs in O(log(n))
     * @param index the location to be relocated
     */
    private void swimDown(int index) {
        double leftCpriority = heap.get(leftChild(index)).getPriority();
        double rightCpriority = heap.get(rightChild(index)).getPriority();
        double parentPriority = heap.get(index).getPriority();

        if (leftCpriority < rightCpriority && leftCpriority < parentPriority) {
            swap(index, leftChild(index));
            swimDown(leftChild(index)); // recursive call to continue relocating node if necessary
        } else if (rightCpriority < leftCpriority && leftCpriority < parentPriority) {
            swap(index, rightChild(index));
            swimDown(rightChild(index)); // recursive call to continue relocating node if necessary
        } else if (leftCpriority == rightCpriority && leftCpriority < parentPriority) {
            // tie breaker
            swap(index, leftChild(index));
            swimDown(leftChild(index));
        }
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(1)
     * @return the size of the heap
     */
    @Override
    public int size() {
        return size;
    }

    /** Changes the priority of the given item. Throws NoSuchElementException if
     * the item doesn't exist.
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("No item in heap");
        }
        int targetNode = map.get(item); // get the index of item in heap
        heap.get(targetNode).setPriority(priority); // set new priority
        swimUp(targetNode);
        swimDown(targetNode);
    }

    /**
     * Determines the index of the parent of given index
     * @param index of child node
     * @return index of parent node
     */
    private int parent(int index) {
        if (index == root) {
            return root; // special case
        }
        return index / 2;
    }

    /**
     * Determines the index of left child of given index
     * If note at index does not have children, it returns
     * the given index
     * @param index of parent node
     * @return index of left child node
     */
    private int leftChild(int index) {
        int leftC = index * 2;
        return (leftC > size) ? index : leftC;
    }

    /**
     * Determines the index of right child of given index
     * If note at index does not have children, it returns
     * the given index
     * @param index of parent node
     * @return index of right child node
     */
    private int rightChild(int index) {
        int rightC = index * 2 + 1;
        return (rightC > size) ? index : rightC;
    }

    /**
     * Returns array of items
     * Used for debugging purposes
     * @return
     */
    public T[] getItemArray() {
        T[] items = (T[]) new Object[heap.size()];
        int i = 0;
        for (PriorityNode<T> node : heap) {
            items[i++] = node.getItem();
        }
        return items;
    }

    /**
     * Returns array of priorities
     * Used for debugging purposes
     * @return
     */
    public Double[] getPriorityArray() {
        Double[] priorities = new Double[heap.size()];
        int i = 0;
        for (PriorityNode<T> node : heap) {
            priorities[i++] = Double.valueOf(node.getPriority());
        }
        return priorities;
    }

    /**
     * Represents a node of the ExtrinsicMinPQ
     */
    private class PriorityNode<T> {
        private T item;
        private double priority;

        PriorityNode(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double newPriority) {
            this.priority = newPriority;
        }

        T getItem() {
            return item;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            PriorityNode<T> that = (PriorityNode<T>) o;
            if (!this.item.equals(that.getItem()) || this.priority != that.getPriority()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

    }
}
