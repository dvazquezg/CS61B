package bearmaps.proj2c;

//import bearmaps.hw4.WeirdSolver;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
//import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Daniel Vazquez
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    //private KDTree kdTree;
    private HashMap<Point, Node> vertex;
    private WeirdPointSet weirdPS;
    private MyTrieSet magicTrie;
    private HashMap<String, String> trieToPlace;
    private HashMap<String, Node> magicMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        // create list of points
        ArrayList<Point> points = new ArrayList<>();
        // add points to KDTree
        vertex = new HashMap<>();
        magicTrie = new MyTrieSet();
        trieToPlace = new HashMap<>();
        magicMap = new HashMap<>();

        // fill out data structures
        for (Node node: nodes) {
            // avoid vertex with no neighbors
            if (this.neighbors(node.id()).size() != 0) {
                Point newPoint = new Point(node.lon(), node.lat());
                points.add(newPoint);
                vertex.put(newPoint, node);
            }

            //--- gold points
            if (node.name() != null) {
                String cleanedName = cleanString(node.name());
                magicTrie.add(cleanedName);
                trieToPlace.put(cleanedName, node.name());
                magicMap.put(node.name(), node);
            }

        }
        //System.out.println("POINTS: " + points.size());
        // feed kdTree with point list
        //kdTree = new KDTree(points);
        weirdPS = new WeirdPointSet(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        //Point closest = kdTree.nearest(lon, lat);
        Point closest = weirdPS.nearest(lon, lat);
        return vertex.get(closest).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> keys = magicTrie.keysWithPrefix(cleanString(prefix));
        List<String> fullNames = new LinkedList<>();
        for (String key : keys) {
            fullNames.add(trieToPlace.get(key));
        }
        return new LinkedList<>();
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> magicList = new ArrayList<>();
        List<String> places = getLocationsByPrefix(locationName);

        for (String place : places) {
            Map<String, Object> result = new HashMap<>();
            Node node = magicMap.get(place);
            result.put("lat", node.lat());
            result.put("lon", node.lon());
            result.put("name", node.name());
            result.put("id", node.id());
            magicList.add(result);
        }

        return magicList;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
