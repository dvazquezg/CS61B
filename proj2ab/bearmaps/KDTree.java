package bearmaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a K-d tree
 * @author Daniel Vazquez
 */
public class KDTree implements PointSet {
    private Node root;
    private int size;

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
        //Collections.shuffle(temp); // shuffle points to make K-d tree more balanced
        // insert points one-by-one
        for (Point point: temp) {
            root = add(root, point, Axis.Yaxis);
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
        // this.point < point: neg
        int nodePointVSnewPoint = node.comparePoint(newPoint);
        if (nodePointVSnewPoint < 0) {
            node.rightChild = add(node.rightChild, newPoint, flipAx(node.getAxis()));
        } else if (nodePointVSnewPoint > 0) {
            node.leftChild = add(node.leftChild, newPoint, flipAx(node.getAxis()));
        } else {
            node.point = newPoint; // overwrite
        }
        size += 1;
        return node;
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
        System.out.println(current.getPoint() + "->" + current.distance(target)
                + "||" + closest.getPoint() + "->" + closest.distance(target));
        // check if distance of current node is better than closest so far
        if (current.distance(target) < closest.distance(target)) {
            closest = current;
        }
        // compare current's point and targetPoint
        int nodePointVStarget = current.comparePoint(target);
        Node bestChild;
        Node badChild;

        // determine which children of the current node is best suited
        if (nodePointVStarget < 0) { // current is smaller than target
            bestChild = current.rightChild; // best could be located in the "right/up" side
            badChild = current.leftChild;
        } else if (nodePointVStarget > 0) {
            bestChild = current.leftChild; // best could be located in the "left/down" side
            badChild = current.rightChild;
        } else {
            return current; // the current point is the same as the target point
        }

        // find a better point is the better side
        closest = nearest(bestChild, target, closest);

        // check if bad side could have a better point
        if (badChild != null && closest.isThereCloser(target, closest)) {
            closest = nearest(badChild, target, closest);
        }

        return closest;
    }

    /**
     * Axis for 2-Dimension
     */
    private enum Axis { Yaxis, Xaxis }


    public void print(){
        root.print();
    }

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

        /**
         * Determines if this node's region may contain a closer point
         * @param target the desired point
         * @param closest current closets node
         * @return true is it may contain a closer point, false otherwise
         */
        public boolean isThereCloser(Point target, Node closest) {
            double shortestDistance;
            if (this.compareBy.equals(Axis.Yaxis)) {
                shortestDistance = (target.getX() - this.getX()) * (target.getX() - this.getX());
            } else {
                shortestDistance = (target.getY() - this.getY()) * (target.getY() - this.getY());
            }
            return shortestDistance < closest.distance(target);
        }


        public void print() {
            print("", true, "root ", 1);
        }

        private void print(String prefix, boolean isTail, String name, int level) {
            String info = name + level + ": <" + this.compareBy + "> "
                        + "(" + getX() + ", " + getY() + ")";
            System.out.println(prefix + (isTail ? "└── " : "├── ") + info);
            if (leftChild != null && rightChild == null) {
                leftChild.print(prefix + (isTail ?"    " : "│   "), true, "Left  ", level + 1);
            } else if (leftChild != null && rightChild != null) {
                leftChild.print(prefix + (isTail ? "    " : "│   "), false, "Left  ", level + 1);
            }

            if (rightChild != null && leftChild == null) {
                rightChild.print(prefix + (isTail ?"    " : "│   "), true, "Right ", level + 1);
                //rightChild.print(prefix + (isTail ? "    " : "│   "), false);
            } else if (rightChild != null && leftChild != null) {
                rightChild.print(prefix + (isTail ? "    " : "│   "), false, "Right ", level + 1);
            }
        }

        /**
         * Compares this Node's point and a given point
         * If both x and y coordinates of both points are the same, then
         * points are the same. Otherwise it compares points by this node's
         * axis of comparison
         * @param p the point to be compared
         * @return result of comparison
         */
        public int comparePoint(Point p) {
            if (this.point.equals(p)) {
                return 0; // return zero if the point are the same in BOTH dimensions
            }
            int comparison;
            if (this.compareBy.equals(Axis.Xaxis)) { // Compares ONE dimension
                comparison =  Double.compare(this.getY(), p.getY());
            } else {
                comparison =  Double.compare(this.getX(), p.getX());
            }
            // break ties by adding one if the comparison is zero (same dimension)
            // if parent and child are the same in given axis, parent(this) is smaller than child
            return comparison == 0 ? -1 : comparison;
        }
    }
}
