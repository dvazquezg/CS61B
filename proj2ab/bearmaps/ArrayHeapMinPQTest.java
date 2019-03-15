package bearmaps;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    @Test
    public void testAdd() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        assertEquals(0, heap.size());

        heap.add(1, 1);
        heap.add(2, 5);
        heap.add(3, 1);
        heap.add(4, 6);
        heap.add(5, 5);
        heap.add(6, 6);
        heap.add(7, 3);
        heap.add(8, 7);
        heap.add(9, 7);
        heap.add(10, 8);
        assertEquals(10, heap.size());
        // swim ups from here
        heap.add(11, 3);
        heap.add(12, 5);
        assertEquals(12, heap.size());

        //PrintHeapDemo.printSimpleHeapDrawing(heap.getPriorityArray());
        PrintHeapDemo.printFancyHeapDrawing(heap.getPriorityArray());
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        assertEquals(0, heap.size());

        heap.add(1, 1); // first smallest to be removed
        heap.add(2, 5);
        heap.add(3, 1); // second smallest to be removed
        heap.add(4, 6);
        heap.add(5, 5);
        heap.add(6, 6);
        heap.add(7, 3); // 3rd smallest on right branch
        heap.add(8, 7);
        heap.add(9, 7);
        heap.add(10, 8);
        heap.add(11, 3); // 3rd smallest on left branch
        heap.add(12, 5);
        assertEquals(12, heap.size());
        assertEquals(Integer.valueOf(1), heap.removeSmallest());
        assertEquals(11, heap.size());
        assertEquals(Integer.valueOf(3), heap.getSmallest());
        assertEquals(Integer.valueOf(3), heap.removeSmallest());
        assertEquals(10, heap.size());
        assertEquals(Integer.valueOf(11), heap.getSmallest()); // tie-breaker: swim up left child

        //PrintHeapDemo.printSimpleHeapDrawing(heap.getPriorityArray());
        PrintHeapDemo.printFancyHeapDrawing(heap.getItemArray());
        PrintHeapDemo.printFancyHeapDrawing(heap.getPriorityArray());
    }
}
