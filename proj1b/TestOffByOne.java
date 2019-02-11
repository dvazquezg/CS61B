import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualsChars() {
        assertTrue(offByOne.equalChars('x', 'y'));
        assertFalse(offByOne.equalChars('a', 'a'));
        assertFalse(offByOne.equalChars('a', 'c'));
        assertFalse(offByOne.equalChars('a', 'B'));
        assertTrue(offByOne.equalChars('K', 'L'));
        assertTrue(offByOne.equalChars('&', '%'));
        assertFalse(offByOne.equalChars('$', '+'));
        assertFalse(offByOne.equalChars('D', 'D'));
        assertFalse(offByOne.equalChars('=', '='));
        assertFalse(offByOne.equalChars('a', '#'));
    }
}
