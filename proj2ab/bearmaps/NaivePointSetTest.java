package bearmaps;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;

public class NaivePointSetTest {
    @Test
    public void naiveTest() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        ret.getX(); // evaluates to 3.3
        ret.getY(); // evaluates to 4.4
        assertEquals(3.3, ret.getX(), 0.001);
        assertEquals(4.4, ret.getY(), 0.001);
    }
}
