package bearmaps.hw4;

import bearmaps.hw4.AStarGraph;
import bearmaps.hw4.ShortestPathsSolver;
import bearmaps.hw4.SolverOutcome;
import bearmaps.hw4.WeightedEdge;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bearmaps.proj2ab.DoubleMapPQ;

/**
 * Implementation of the A* algorithm with memory optimization.
 * @author Daniel Vazquez
 * @version 1.0
 * @param <Vertex> the generic vertex type
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    // A* Algorithm analysis instance variables
    private HashMap<Vertex, Double> distTo; // best known total distance from source to vertex
    private HashMap<Vertex, Vertex> edgeTo; // maps each vertex to best known predecessor
    //private DoubleMapPQ<Vertex> fringe;
    private ArrayHeapMinPQ<Vertex> fringe;

    // outcome intance varibles
    private SolverOutcome outcome; // The final status of the A* algorithm
    private double solutionWeight; // The total weight of the given solution
    private List<Vertex> solution; // A list of vertices corresponding to a solution
    private int numStatesExplored; // The total number of priority queue dequeue operations
    private double timeSpent; // The total time spent in seconds by the constructor

    /**
     * Executes the A* algorithm on the given graph.
     * It finds the solution, computing everything necessary for all
     * other methods to return their results in constant time.
     * @param input the graph to be analyzed
     * @param start the starting vertex
     * @param end the goal/destination vertex
     * @param timeout the time limit of execution in seconds
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        // initialize instance variables
        //fringe = new DoubleMapPQ<>(); // priority queue
        fringe = new ArrayHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        solution = new ArrayList<>();
        numStatesExplored = 0;
        solutionWeight = 0;
        timeSpent = 0;

        // add source node to fringe and maps
        fringe.add(start, input.estimatedDistanceToGoal(start, end)); // add first node
        distTo.put(start, 0.0); // distance to itself
        edgeTo.put(start, null); // source does not have predecessor

        // call main solver method
        solver(input, start, end, timeout);
    }

    private void solver(AStarGraph<Vertex> input, Vertex source, Vertex goal, double timeout) {
        Stopwatch sw = new Stopwatch(); // start timer
        Vertex current = null;
        // main loop
        while (fringe.size() != 0 && (timeSpent = sw.elapsedTime()) < timeout) {
            current = fringe.removeSmallest();
            numStatesExplored++; // count dequeue operations
            if (current.equals(goal)) {
                solutionGenerator(source, goal);
                outcome = SolverOutcome.SOLVED;
                timeSpent = sw.elapsedTime();
                return;
            }
            // get neighbors edges
            List<WeightedEdge<Vertex>> neighborEdges = input.neighbors(current);
            for (WeightedEdge<Vertex> e : neighborEdges) {
                // relax edge and add to fringe PQ
                edgeRelaxer(e, input, goal);
            }
        }
        // if reached this point, A* was unable to solve the problem
        // check if time ran out
        if (timeSpent >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
        } else {
            // this means that fringe PQ became empty before solution was found
            outcome = SolverOutcome.UNSOLVABLE;
        }

    }

    /**
     * Generates the list vertex corresponding to the shortest path
     * found by A* and assigns the total weight (i.e. distance)
     * between source and goal
     */
    public void solutionGenerator(Vertex source, Vertex goal) {
        solution = new ArrayList<>(); // the list of results.
        solutionWeight = distTo.get(goal); // total
        Vertex current = goal; // start building list to from to source
        while (current != null) {
            solution.add(0, current);
            current = edgeTo.get(current);
        }
    }

    /**
     * Relaxes a given edge
     * Relaxation refers to the action of updating the distance from source
     * to vertex q if distTo[p] + w < distTo[q]. It also performs fringe PQ
     * enqueuing operations and employs the heuristic function to set
     * the priority of a Vertex in the fringe. The priority is give by:
     * distTo[q] + h(q, goal) where h is the heuristic estimate
     * @param e
     */
    private void edgeRelaxer(WeightedEdge<Vertex> e, AStarGraph<Vertex> input, Vertex goal) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();
        if (!distTo.containsKey(e.to())) {
            // if vertex q is not recorded, add it to maps and fringe PQ
            distTo.put(q, distTo.get(p) + w);
            edgeTo.put(q, p);
            fringe.add(q, distTo.get(q) + input.estimatedDistanceToGoal(q, goal));
        } else {
            // if vertex q exist, check if need relaxation
            double newDistance = distTo.get(p) + w;
            if (newDistance < distTo.get(q)) {
                distTo.put(q, newDistance);
                edgeTo.put(q, p);
                // check if vertex q is in fringe PQ
                if (fringe.contains(q)) {
                    // change priority using distance to q and the heuristic estimate
                    fringe.changePriority(q, distTo.get(q)
                            + input.estimatedDistanceToGoal(q, goal));
                }
            }

        }
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() {
        return solution;
    }

    public double solutionWeight() {
        return solutionWeight;
    }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return timeSpent;
    }
}
