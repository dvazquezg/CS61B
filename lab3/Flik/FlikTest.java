import static org.junit.Assert.*;
import org.junit.Test;
public class FlikTest {
    @Test
    public void isSameNumberTest(){
        int a = 2;
        int b = 6;
        int c = 2;
        int d = 128;
        int e = 128;

        assertFalse(Flik.isSameNumber(a, b));
        assertTrue(Flik.isSameNumber(a, c));
        assertFalse(Flik.isSameNumber(b, c));
        assertTrue(Flik.isSameNumber(d, e));
    }
}
