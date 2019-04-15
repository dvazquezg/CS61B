package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a K-d tree
 * @author Daniel Vazquez
 */
public class KDTree implements PointSet {
    private Node root;

    /**
     * Creates a Kd tree from a list of nodes
     * @param points list of points
     */
    public KDTree(List<Point> points) {
        if (points == null) {
            throw new IllegalArgumentException("List must contain points");
        }
        ArrayList<Point> temp = new ArrayList<>();
        for (Point p: points) {
            temp.add(p);
        }
        Collections.shuffle(temp); // shuffle points to make K-d tree more balanced
        // insert points one-by-one
        for (Point point: temp) {
            root = add(root, point, Axis.Xaxis);
        }
    }

    /**
     * Adds a new point to the tree
     * @param node parent node
     * @param newPoint point to be inserted
     * @param pAxis parent split axis
     * @return node
     */
    private Node add(Node node, Point newPoint, Axis pAxis) {
        if (node == null) {
            return new Node(newPoint, pAxis);
        }
        double comparison = comparePoints(newPoint, node.point, pAxis);

        if (comparison < 0) {
            node.leftChild = add(node.leftChild, newPoint, flipAx(pAxis));

        } else if (comparison >= 0) {
            node.rightChild = add(node.rightChild, newPoint, flipAx(pAxis));
        }

        return node;
    }

    /**
     * Compares two points based on the axis of parent node
     * @param newP the point to insert
     * @param current the parent node
     * @param pAxis the parent's axis of comparison
     * @return
     */
    private double comparePoints(Point newP, Point current, Axis pAxis) {
        if (pAxis.equals(Axis.Xaxis)) {
            return Double.compare(newP.getX(), current.getX());
        } else {
            return Double.compare(newP.getY(), current.getY());
        }
    }

    /**
     * Flips the returns the opposite 2D axis
     * @param axis
     * @return the flipped axis
     */
    private Axis flipAx(Axis axis) {
        if (axis.equals(Axis.Xaxis)) {
            return Axis.Yaxis;
        } else {
            return Axis.Xaxis;
        }
    }

    @Override
    public Point nearest(double x, double y) {
        return nearest(root, new Point(x, y), root).getPoint();

    }

    private Node nearest(Node current, Point target, Node closest) {
        if (current == null) {
            return closest;
        }
        // check if distance of current node is better than closest so far
        if (current.distance(target) < closest.distance(target)) {
            closest = current;
        }
        // compare current's point and targetPoint
        double compareTarget = comparePoints(target, current.point, current.getAxis());
        Node bestChild = null;
        Node badChild = null;

        // determine which children of the current node is best suited
        if (compareTarget < 0) { // target is smaller than current
            bestChild = current.leftChild; // best could be located in the "left/down" side
            badChild = current.rightChild;
        } else if (compareTarget >= 0) {
            bestChild = current.rightChild; // best could be located in the "right/up" side
            badChild = current.leftChild;
        }

        // find a better point is the better side
        closest = nearest(bestChild, target, closest);

        // check if bad side of current node could have something better
        if (badChild != null && isThereCloser(current, target, closest)) {
            closest = nearest(badChild, target, closest);
        }

        // returns closest point found
        return closest;
    }

    /**
     * Determines if given current node's region may contain a closer point
     * @param current the node to check
     * @param target the desired point
     * @param closest current closets node
     * @return true is it may contain a closer point, false otherwise
     */
    public boolean isThereCloser(Node current, Point target, Node closest) {
        double shortestDistance;
        if (current.compareBy.equals(Axis.Xaxis)) {
            shortestDistance = (target.getX() - current.getX()) * (target.getX() - current.getX());
        } else {
            shortestDistance = (target.getY() - current.getY()) * (target.getY() - current.getY());
        }
        // compares the shortest distance of current node's region to distance to closest
        return shortestDistance < closest.distance(target);
    }

    /**
     * Axis for 2-Dimension
     */
    private enum Axis { Yaxis, Xaxis }

    /*
    public void print(){
        root.print();
    }*/

    /**
     * Class that represents a Point stored in the KDTree
     */
    private class Node {
        private Point point;
        private Axis compareBy;
        private Node rightChild;
        private Node leftChild;

        /**
         * Creates a Node by passing both point and parent node.
         * The parent node will be used to determine the split axis
         * @param point
         * @param parentAxis
         */
        Node(Point point, Axis parentAxis) {
            this.point = point;
            this.compareBy = parentAxis;
        }

        public Axis getAxis() {
            return compareBy;
        }

        /**
         * Get X coordinate of point
         * @return the coordinate
         */
        public double getX() {
            return point.getX();
        }

        /**
         * Get Y coordinate of point
         * @return the cooordinate
         */
        public double getY() {
            return point.getY();
        }


        public Point getPoint() {
            return point;
        }

        /**
         * Return the squared distance between two points
         * @param target
         * @return thr distance
         */
        public double distance(Point target) {
            double deltaX = this.getX() - target.getX();
            double deltaY = this.getY() - target.getY();
            return deltaX * deltaX + deltaY * deltaY;
        }

        /*
        public void print() {
            print("", true, "root ", 1);
        }

        private void print(String prefix, boolean isTail, String name, int level) {
            String info = name + level + ": <" + this.compareBy + "> "
                        + "(" + getX() + ", " + getY() + ")";
            System.out.println(prefix + (isTail ? "└── " : "├── ") + info);
            if (leftChild != null && rightChild == null) {
                leftChild.print(prefix + (isTail ? "    " : "│   "), true, "Left  ", level + 1);
            } else if (leftChild != null && rightChild != null) {
                leftChild.print(prefix + (isTail ? "    " : "│   "), false, "Left  ", level + 1);
            }

            if (rightChild != null && leftChild == null) {
                rightChild.print(prefix + (isTail ? "    " : "│   "), true, "Right ", level + 1);
                //rightChild.print(prefix + (isTail ? "    " : "│   "), false);
            } else if (rightChild != null && leftChild != null) {
                rightChild.print(prefix + (isTail ? "    " : "│   "), false, "Right ", level + 1);
            }
        }*/
    }
}
