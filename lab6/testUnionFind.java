import static org.junit.Assert.*;
import org.junit.Test;

public class testUnionFind {
    @Test
    public void testConnected() {
        UnionFind dset = new UnionFind(16);
        assertEquals(1, dset.sizeOf(1));
        dset.union(3, 2);
        assertEquals(2, dset.find(3));
        dset.union(8, 9);
        assertEquals(2, dset.sizeOf(9));
        dset.union(2, 8);
        dset.union(2, 4);
        assertEquals(9, dset.find(3));
        assertEquals(5, dset.sizeOf(8));
        dset.union(5, 6);
        dset.union(6, 7);
        assertEquals(6, dset.find(7));
        assertEquals(3, dset.sizeOf(6));
        dset.union(7, 8);
        assertEquals(8, dset.sizeOf(5));
        assertTrue(dset.connected(7, 3));
        assertEquals(1, dset.sizeOf(15));
        dset.union(1, 14);
        dset.union(13, 15);
        dset.union(15, 1);
        assertEquals(14, dset.find(1));
        assertEquals(4, dset.sizeOf(13));
        dset.union(8, 13);
        assertTrue(dset.connected(1, 5));
        assertEquals(9, dset.find(1));
        assertEquals(12, dset.sizeOf(13));
        assertFalse(dset.connected(0, 10));
        dset.union(12, 11);
        dset.union(10, 0);
        dset.union(0, 12);
        assertEquals(11, dset.find(10));
        assertEquals(4, dset.sizeOf(12));
        assertTrue(dset.connected(10, 12));
        assertFalse(dset.connected(7, 10));
        dset.union(10, 13);
        assertEquals(9, dset.find(12));
        assertEquals(16, dset.sizeOf(3));
        assertTrue(dset.connected(7, 10));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidIndex() {
        UnionFind dset = new UnionFind(16);
        dset.sizeOf(-1);
    }
}
