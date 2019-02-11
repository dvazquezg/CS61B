import static org.junit.Assert.*;
import org.junit.Test;

public class TestOffByN {
    //static CharacterComparator offByN = new OffByN();
    @Test
    public void testEqualChars() {
        assertTrue((new OffByN(5)).equalChars('a', 'f'));
        assertTrue((new OffByN(5)).equalChars('f', 'a'));
        assertTrue((new OffByN(5)).equalChars('d', 'i'));
        assertFalse((new OffByN(5)).equalChars('a', 'b'));
        assertFalse((new OffByN(5)).equalChars('a', 'a'));
        assertTrue((new OffByN(3)).equalChars('d', 'a'));
        assertTrue((new OffByN(1)).equalChars('x', 'y'));
        assertFalse((new OffByN(1)).equalChars('a', 'a'));
        assertFalse((new OffByN(1)).equalChars('a', 'c'));
        assertTrue((new OffByN(1)).equalChars('a', 'b'));
        assertTrue((new OffByN(1)).equalChars('w', 'x'));
        assertTrue((new OffByN(1)).equalChars('r', 'q'));
        assertFalse((new OffByN(1)).equalChars('z', 'a'));
    }
    
}
