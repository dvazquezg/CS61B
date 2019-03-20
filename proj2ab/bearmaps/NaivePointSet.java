package bearmaps;

import java.util.ArrayList;
import java.util.List;

/**
 * A naive implementation to find closest point
 * @author Daniel Vazquez
 */
public class NaivePointSet implements PointSet {
    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = new ArrayList<>();
        for (Point p: points) {
            this.points.add(p);
        }
    }

    /**
     * Evaluates the squared Euclidean distance closets to given coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the squared distance
     */
    @Override
    public Point nearest(double x, double y) {
        Point closestP = points.get(0), targetP = new Point(x, y);
        double currDis, minDis = Point.distance(closestP, targetP); // first distance
        for (Point currentP: points) {
            currDis = Point.distance(currentP, targetP);
            if (currDis < minDis) {
                minDis = currDis;
                closestP = currentP;
            }
        }
        return closestP;
    }
}
