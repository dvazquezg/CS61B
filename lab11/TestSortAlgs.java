import edu.princeton.cs.algs4.Queue;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<Integer> numbers = new Queue<>();
        numbers.enqueue(32);
        numbers.enqueue(15);
        numbers.enqueue(2);
        numbers.enqueue(17);
        numbers.enqueue(19);
        numbers.enqueue(41);
        numbers.enqueue(17);
        numbers.enqueue(17);

        System.out.println("Unsorted: " + numbers);
        assertFalse(numbers.isEmpty());
        // call merge sort
        Queue<Integer> sorted = QuickSort.quickSort(numbers);
        assertFalse(numbers.isEmpty());
        System.out.println("Sorted: " + sorted);
        assertTrue(isSorted(sorted));
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> numbers = new Queue<>();
        numbers.enqueue(32);
        numbers.enqueue(15);
        numbers.enqueue(2);
        numbers.enqueue(17);
        numbers.enqueue(19);
        numbers.enqueue(41);
        numbers.enqueue(17);
        numbers.enqueue(17);
        numbers.enqueue(0);

        System.out.println("Unsorted: " + numbers);
        assertFalse(numbers.isEmpty());
        // call merge sort
        Queue<Integer> sorted = MergeSort.mergeSort(numbers);
        assertFalse(numbers.isEmpty());
        System.out.println("Sorted: " + sorted);
        assertTrue(isSorted(sorted));

        //----------- Additional test -------------
        numbers = new Queue<>();
        numbers.enqueue(38);
        numbers.enqueue(27);
        numbers.enqueue(43);
        numbers.enqueue(3);
        numbers.enqueue(9);
        numbers.enqueue(82);
        numbers.enqueue(10);
        System.out.println("Unsorted: " + numbers);
        sorted = MergeSort.mergeSort(numbers);
        System.out.println("Sorted: " + sorted);
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
