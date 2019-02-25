package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Daniel Vazquez Guevara
 */

public class TestArrayRingBuffer {
    /***
     * Implementing some enqueueing and dequeuing test
     */
    @Test
    public void someTest() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<>(4);
        arb.enqueue(9.3);    // 9.3
        arb.enqueue(15.1);   // 9.3  15.1
        arb.enqueue(31.2);   // 9.3  15.1  31.2
        assertFalse(arb.isFull()); // 9.3  15.1  31.2      (returns false)
        arb.enqueue(-3.1);  // 9.3  15.1  31.2  -3.1
        assertTrue(arb.isFull()); // 9.3  15.1  31.2  -3.1 (returns true)
        Double d1 = arb.dequeue(); // 15.1 31.2  -3.1      (returns 9.3)
        assertEquals(9.3, d1, 0.01);
        arb.enqueue(28.0);    // 28.0 15.1 31.2  -3.1
        Double pk = arb.peek();          // 28.0 15.1 31.2  -3.1  (returns 15.1)
        assertEquals(15.1, pk, 0.01);
    }

    @Test(expected = Exception.class)
    public void testOverflow() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<>(4);
        arb.enqueue(9.3);    // 9.3
        arb.enqueue(15.1);   // 9.3  15.1
        arb.enqueue(31.2);   // 9.3  15.1  31.2
        arb.enqueue(28.0);   // 28.0 15.1 31.2  -3.1
        arb.enqueue(7.0);    // Overflow!
    }

    @Test(expected = Exception.class)
    public void testUnderflow() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<>(2);
        arb.enqueue(9.3);    // 9.3
        arb.enqueue(15.1);   // 9.3  15.1
        arb.dequeue();
        arb.dequeue();
        arb.dequeue(); // Underflow
    }

    @Test
    public void testIteratorEquals() {
        ArrayRingBuffer<Double> arb1 = new ArrayRingBuffer<>(4);
        arb1.enqueue(9.3);    // 9.3
        arb1.enqueue(15.1);   // 9.3  15.1
        arb1.enqueue(31.2);   // 9.3  15.1  31.2
        arb1.enqueue(28.0);   // 28.0 15.1 31.2  -3.1

        ArrayRingBuffer<Double> arb2 = new ArrayRingBuffer<>(4);
        arb2.enqueue(9.3);    // 9.3
        arb2.enqueue(15.1);   // 9.3  15.1
        arb2.enqueue(31.2);   // 9.3  15.1  31.2
        arb2.enqueue(28.0);   // 28.0 15.1 31.2  -3.1

        ArrayRingBuffer<Double> arb3 = new ArrayRingBuffer<>(4);
        arb3.enqueue(5.6);    // 5.6
        arb3.enqueue(6.7);   // 5.6  6.7
        arb3.enqueue(4.2);   // 5.6  6.7 4.2
        arb3.enqueue(9.1);   // 28.0 6.7 4.2  9.1

        assertTrue(arb1.equals(arb2));
        assertTrue(arb1.equals(arb1));
        assertTrue(arb2.equals(arb2));
        assertFalse(arb1.equals(arb3));
    }

}
