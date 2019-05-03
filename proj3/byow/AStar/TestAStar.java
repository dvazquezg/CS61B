package byow.AStar;

import byow.Core.*;
import byow.TileEngine.TETile;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestAStar {
    private TileMapGraph worldMap;
    private TETile[][] world;
    private int width = 10;
    private int height = 10;

    ArgumentAnalyzer analyzer = new ArgumentAnalyzer("N3412S");

    @Test
    public void sanityCheck() {
        // check if given input is valid
        worldMap = new TileMapGraph();

        assertTrue(analyzer.success());

        Game game = new Game(width, height);
        game.executeArgument(analyzer);
        world = game.getWorld();

        printWorld(world);

        graphFiller();

        //System.out.println(worldMap.vertices().size());
        //worldMap.vertices().
        int testPoint = 14;
        System.out.println(worldMap.getPoints().get(testPoint));

        System.out.println(worldMap.getPoints().get(testPoint).equals(new SimplePoint(4, 8)));

        System.out.println("Neighbors: " + worldMap.neighbors(testPoint).size());

        for (WeightedEdge<Integer> edge : worldMap.neighbors(testPoint)) {
            System.out.println(edge);

        }

        System.out.println("Get Id: " + worldMap.getId(new SimplePoint(4, 8)));

        SimplePoint[][] grid = new SimplePoint[height][width];
        int id = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = new SimplePoint(row, col, id);
                id++;
            }
        }

        AStarSolver<Integer> solver = new AStarSolver<>(worldMap, 14, 75, 20);

        SolutionPrinter.summarizeSolution(solver, "->");
    }

    private void graphFiller() {
        int id = 0;
        for (int i = world[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < world.length; j++) {
                if (world[j][i].equals(Constants.FLOORTILE)) {
                    SimplePoint curPoint = new SimplePoint(j, i, id);
                    worldMap.addNode(curPoint);
                    addNeighbors(curPoint);
                }
                //id ++;
                id += 1;
                //System.out.print(count + " " + (count < 10 ? " ":""));
            }
            //System.out.println();
        }
        edgeFiller(); // add nodes
    }

    private void edgeFiller() {
        for (SimplePoint point: worldMap.getPoints().values()) {
            addNeighbors(point);
        }
    }

    private void addNeighbors(SimplePoint curPoint) {
        int idNeighbor;
        for (int j = 1; j >= -1; j--) {
            idNeighbor = curPoint.getId() + width * -j;
            for (int i = -1; i <= 1; i++) {
                // get new coordinated of surrounding neighbors
                int xNeighbor = curPoint.getXpos() + i;
                int yNeighbor = curPoint.getYpos() + j;
                // check if new coords are within the world
                if (xNeighbor >= 0 && xNeighbor < width && yNeighbor >= 0 && yNeighbor < height) {
                    //System.out.print("(" + xNeighbor + ", " + yNeighbor + ")");
                    // skip current Point
                    if (!(xNeighbor == curPoint.getXpos() && yNeighbor == curPoint.getYpos())){
                        //System.out.println("ok....");
                        //System.out.print(world[xNeighbor][yNeighbor]);
                        if (world[xNeighbor][yNeighbor].equals(Constants.FLOORTILE)) {
                            //System.out.print(world[xNeighbor][yNeighbor]);
                            //System.out.print(">>" + curPoint.getId() + ", " + (idNeighbor + i) + "<<");
                            worldMap.addWeightedEdge(curPoint.getId(), idNeighbor + i);
                        }
                    }

                }
            }
            //System.out.println();
        }

        //System.out.println();
    }


    public void printWorld(TETile[][] finalWorldFrame) {
        if (finalWorldFrame == null) {
            System.out.println("The gird is empty");
            return;
        }
        String grid = "";
        int id = 0;
        for (int i = finalWorldFrame[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < finalWorldFrame.length; j++) {
                if (finalWorldFrame[j][i].toString().equals("·")) {
                    grid += id + " ";
                    //id ++;
                } else {
                    grid += finalWorldFrame[j][i].toString() + "  ";
                }

                id ++;
            }
            grid += "\n"; // new line
        }

        System.out.println(grid);
    }

    private void printGrid(SimplePoint[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                System.out.print(grid[row][col].getId() + " "
                        + (grid[row][col].getId() < 10 ? " ":""));
            }
            System.out.println();
        }
    }

}
