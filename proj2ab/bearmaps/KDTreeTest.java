package bearmaps;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTreeTest {
    @Test
    public void constructorTest() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);
        List<Point> pointsDemo = List.of(p1, p2, p3, p4, p5, p6, p7);

        // generate pointStructures
        NaivePointSet naiveSet = new NaivePointSet(pointsDemo);
        KDTree kdTree = new KDTree(pointsDemo);

        // generate target point
        double x = 0;
        double y = 7;
        Point targetPoint = new Point(x, y);

        // get closets point
        Point closestNaive = naiveSet.nearest(x, y);
        Point closestKDtree = kdTree.nearest(x, y);

        //System.out.println("Target point: " + targetPoint);
        //System.out.println(closestNaive);
        //System.out.println(closestKDtree);
        //kdTree.print();

        // verify that distances are the same
        //assertTrue(closestNaive.equals(closestKDtree));
        double disNaiveTarget = distance(closestNaive, targetPoint);
        double distanceKDTarget = distance(closestKDtree, targetPoint);
        assertEquals(disNaiveTarget, distanceKDTarget, .00000000001);

    }

    @Test
    public void deadlePoints2() {
        Point p1 = new Point(277.5435652488, -450.5567989151);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(187.2792022774, -695.8081816334);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(2, 3);
        Point p6 = new Point(4, 5);
        List<Point> pointsDemo = List.of(p1, p2, p3, p4, p5, p6);

        // generate pointStructures
        NaivePointSet naiveSet = new NaivePointSet(pointsDemo);
        KDTree kdTree = new KDTree(pointsDemo);

        // generate target point
        double x = 427.535670;
        double y = -735.656403;
        Point targetPoint = new Point(x, y);

        // get closets point
        Point closestNaive = naiveSet.nearest(x, y);
        Point closestKDtree = kdTree.nearest(x, y);

        System.out.println("Target point: " + targetPoint);
        System.out.println(closestNaive);
        System.out.println(closestKDtree);
        //kdTree.print();

        // verify that dsitances are the same
        //assertTrue(closestNaive.equals(closestKDtree));
        double disNaiveTarget = distance(closestNaive, targetPoint);
        double distanceKDTarget = distance(closestKDtree, targetPoint);
        assertEquals(disNaiveTarget, distanceKDTarget, .00000000001);
    }

    @Test
    public void deadlyPoints() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(38, 35);
        Point p7 = new Point(38, 37);
        List<Point> pointsDemo = List.of(p1, p2, p3, p4, p5, p6, p7);

        // generate pointStructures
        NaivePointSet naiveSet = new NaivePointSet(pointsDemo);
        KDTree kdTree = new KDTree(pointsDemo);

        // generate target point
        double x = 38;
        double y = 36;
        Point targetPoint = new Point(x, y);

        // get closets point
        Point closestNaive = naiveSet.nearest(x, y);
        Point closestKDtree = kdTree.nearest(x, y);

        System.out.println("Target point: " + targetPoint);
        System.out.println(closestNaive);
        System.out.println(closestKDtree);
        //kdTree.print();

        // verify that distances are the same
        double disNaiveTarget = distance(closestNaive, targetPoint);
        double distanceKDTarget = distance(closestKDtree, targetPoint);
        assertEquals(disNaiveTarget, distanceKDTarget, .00000000001);
    }

    public double distance(Point p1, Point p2) {
        double deltaX = p1.getX() - p2.getX();
        double deltaY = p1.getY() - p2.getY();
        return deltaX * deltaX + deltaY * deltaY;
    }

    @Test
    public void randomTestNaiveKdTree() {
        System.out.println("------- Matching Naive-KD Test -------");
        ArrayList<Point> points = new ArrayList<>();
        int numberOfPoints = rInt(100000, 200000);
        System.out.println("Finding target point among " + numberOfPoints + " random points.");
        // fill list of random points
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(randomPoint(-150, 150));
            //System.out.println(points.get(points.size() -1));
        }

        // generate pointStructures
        NaivePointSet naiveSet = new NaivePointSet(points);
        KDTree kdTree = new KDTree(points);

        //dTree.print();

        // generate random target coordinate
        double x = rInt(-100, 100);
        double y = rInt(-100, 100);
        Point targetPoint = new Point(x, y);

        // get closets point
        Point closestNaive = naiveSet.nearest(x, y);
        Point closestKDtree = kdTree.nearest(x, y);

        System.out.println("Target point: " + targetPoint);
        System.out.println("Closest NaivePointSet: " + closestNaive);
        System.out.println("Closest KDtree: " + closestKDtree);
        System.out.println("Were the outputs the same point? "
                + closestNaive.equals(closestKDtree));

        // verify that distances are the same
        double disNaiveTarget = distance(closestNaive, targetPoint);
        double distanceKDTarget = distance(closestKDtree, targetPoint);
        assertEquals(disNaiveTarget, distanceKDTarget, .00000000001);
        //assertTrue(closestNaive.equals(closestKDtree));
    }

    @Test
    public void timingNaiveKDtree() {
        ArrayList<Point> points = new ArrayList<>();
        // fill list of random points
        for (int i = 0; i < 1000000; i++) {
            points.add(randomPoint(-500, 500));
            //System.out.println(points.get(points.size() -1));
        }
        int queries = 10000; // number of queries for test
        System.out.println("Starting test for " + points.size() + " points.");

        /****** Start NaivePointSet Timing test ******/
        System.out.println("------- NaivePointSet Performance Test -------");

        //------------ INSERT TEST ------------
        long startNaive = System.currentTimeMillis(); // starting time
        NaivePointSet naiveSet = new NaivePointSet(points);
        long endNaive = System.currentTimeMillis(); // ending time
        double insertTimeNaive = (endNaive - startNaive) / 1000.0;
        System.out.println("Total time elapsed to insert " + points.size()
                + " points: " + insertTimeNaive +  " seconds.");

        //------------ QUERY TEST ------------
        startNaive = System.currentTimeMillis(); // starting time
        double x, y;
        for (int i = 0; i < queries; i++) {
            // generate random target coordinate
            x = rDouble(-500, 500);
            y = rDouble(-500, 500);
            // get closets point
            naiveSet.nearest(x, y);
        }

        endNaive = System.currentTimeMillis(); // ending time
        double queryTimeNaive = (endNaive - startNaive) / 1000.0;
        double totalTimeNaive = insertTimeNaive + queryTimeNaive;
        System.out.println("Elapsed time for " + queries
                + " queries to nearest(): " + queryTimeNaive +  " seconds.");
        System.out.println("Total time for NaivePointSet: " + totalTimeNaive + " seconds.");

        /****** End NaivePointSet Timing test ******/

        /********* Start DKtree Timing test ********/
        System.out.println("------- KDtree Performance Test -------");

        //------------ INSERT TEST ------------
        long startKD = System.currentTimeMillis(); // starting time
        KDTree kdTree = new KDTree(points);
        long endKD = System.currentTimeMillis(); // ending time
        double insertTimeKD = (endKD - startKD) / 1000.0;
        System.out.println("Total time elapsed to insert " + points.size()
                + " points: " + insertTimeKD +  " seconds.");

        //------------ QUERY TEST ------------
        startKD = System.currentTimeMillis(); // starting time
        for (int i = 0; i < queries; i++) {
            // generate random target coordinate
            x = rDouble(-500, 500);
            y = rDouble(-500, 500);
            // get closets point
            kdTree.nearest(x, y);
        }
        endKD = System.currentTimeMillis(); // ending time
        double queryTimeKD = (endKD - startKD) / 1000.0;
        double totalTimeKD = insertTimeKD + queryTimeKD;
        System.out.println("Elapsed time for " + queries
                + " queries to nearest(): " + queryTimeKD +  " seconds.");
        System.out.println("Total time for KDtree: " + totalTimeKD + " seconds.");

        /********* End DKtree Timing test ********/
        System.out.println("Additional Stats: ");
        System.out.println("KDtree was " + (int) (insertTimeKD / insertTimeNaive)
                + "x slower than NaivePointSet when inserting.");
        System.out.println("KDtree was " + (int) (queryTimeNaive / queryTimeKD)
                + "x faster than NaivePointSet when querying.");
        System.out.println("KDtree was " + (int) (totalTimeNaive / totalTimeKD)
                + "x faster than NaivePointSet overall.");
        // verify that KDtree is at least 10 times faster querying than NaiveSet
        assertTrue((queryTimeKD / queryTimeNaive) < 0.03);
    }

    /**
     * generates random points
     * @param minX
     * @param maxY
     * @return a point
     */
    public Point randomPoint(double minX, double maxY) {
        return new Point(rDouble(minX, maxY), rDouble(minX, maxY));
    }

    /**
     * generates random integers
     * @param min
     * @param max
     * @return a integer
     */
    public int rInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
    /*
    public void testPointMaker() {
        for (int i = 0; i < 100; i++) {
            //System.out.println(rDouble(-500, 500));
        }
    }*/

    public double rDouble(double min, double max) {
        return StdRandom.uniform(min, max);
    }
}
