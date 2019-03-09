package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import  java.util.ArrayList;
import  java.util.List;

public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        /*
          meaning no two SimpleOomages should EVER have the same
          hashCode UNLESS they have the same red, blue, and green values!
         */

        SimpleOomage ooA1 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB1 = new SimpleOomage(20, 5, 10);
        SimpleOomage ooB2 = new SimpleOomage(15, 15, 5);
        SimpleOomage ooC1 = new SimpleOomage(85, 5, 5);
        SimpleOomage ooC2 = new SimpleOomage(85, 5, 5);
        SimpleOomage ooD1 = new SimpleOomage(5, 60, 55);
        SimpleOomage ooD2 = new SimpleOomage(10, 15, 5);
        SimpleOomage ooE1 = new SimpleOomage(0, 0, 5);
        SimpleOomage ooE2 = new SimpleOomage(0, 5, 0);

        assertEquals(ooA1.hashCode(), ooA2.hashCode());
        assertNotEquals(ooA1.hashCode(), ooB1.hashCode());
        assertNotEquals(ooA2.hashCode(), ooB2.hashCode());
        assertNotEquals(ooB1.hashCode(), ooB2.hashCode());
        assertEquals(ooC1.hashCode(), ooC2.hashCode());
        assertNotEquals(ooD1.hashCode(), ooD2.hashCode());
        assertNotEquals(ooE1.hashCode(), ooE2.hashCode());

    }

    @Test
    public void testEquals() {
        SimpleOomage ooA1 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB1 = new SimpleOomage(10, 5, 20);
        SimpleOomage ooB2 = new SimpleOomage(20, 5, 10);
        SimpleOomage ooC1 = new SimpleOomage(30, 35, 40);
        SimpleOomage ooC2 = new SimpleOomage(40, 35, 30);
        SimpleOomage ooD1 = new SimpleOomage(15, 15, 5);
        SimpleOomage ooD2 = new SimpleOomage(15, 15, 5);

        assertEquals(ooA1, ooA2);
        assertEquals(ooD1, ooD2);
        assertEquals(ooA1, ooA1);
        assertEquals(ooD1, ooD2);
        assertNotEquals(ooA1, ooB2);
        assertNotEquals(ooA2, ooB1);
        assertNotEquals(ooA2, ooB2);
        assertNotEquals(ooC1, ooC2);
        assertNotEquals(ooC2, ooA1);
        assertNotEquals(ooA1, ooD1);
        assertNotEquals(ooD2, ooA2);
        assertNotEquals(ooA1, "ketchup");
    }


    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
