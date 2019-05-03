package byow.AStar;

import byow.Core.SimplePoint;

import java.util.*;

public class TileMapGraph implements AStarGraph<Integer> {

    private Map<Integer, SimplePoint> points = new HashMap<>();
    private Map<Integer, Set<WeightedEdge<Integer>>> neighbors = new HashMap<>();

    public TileMapGraph() {
        // nothing
    }

    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        Set<WeightedEdge<Integer>> incidentSet = neighbors.get(v);
        List<WeightedEdge<Integer>> incidentList = new ArrayList<>();
        for (WeightedEdge<Integer> e : incidentSet) {
            incidentList.add(e);
        }
        return incidentList;
    }

    @Override
    public double estimatedDistanceToGoal(Integer s, Integer goal) {
        SimplePoint sPoint = points.get(s); // source point
        SimplePoint gPoint = points.get(goal); // goal point
        return distance(sPoint.getXpos(), sPoint.getYpos(), gPoint.getXpos(), gPoint.getYpos());
    }

    private double distance(double sx, double sy, double gx, double gy) {
        return Math.pow(gx - sx, 2) + Math.pow(gy - sy, 2); // return distance squared
    }

    public Set<Integer> vertices() {
        Set<Integer> vertices = new HashSet<>();
        for (int id : points.keySet()) {
            vertices.add(id);
        }
        return vertices;
    }

    /** Adds a node to this graph, if it doesn't yet exist. **/
    void addNode(SimplePoint point) {
        if (!points.containsKey(point.getId())) {
            points.put(point.getId(), point);
            neighbors.put(point.getId(), new HashSet<>());
        }
    }

    void addWeightedEdge(int fromID, int toID) {
        if (points.containsKey(fromID) && points.containsKey(toID)) {
            SimplePoint from = points.get(fromID);
            SimplePoint to = points.get(toID);
            double weight = distance(from.getXpos(), to.getYpos(), from.getXpos(), to.getYpos());
            Set<WeightedEdge<Integer>> edgeSet = neighbors.get(fromID);
            edgeSet.add(new WeightedEdge<>(from.getId(), to.getId(), weight));
        }
    }

    public Map<Integer, SimplePoint> getPoints() {
        return points;
    }

    public Map<Integer, Set<WeightedEdge<Integer>>> getNeighbors() {
        return neighbors;
    }
}
