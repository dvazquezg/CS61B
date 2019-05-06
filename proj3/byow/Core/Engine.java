package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

public class Engine {
    private TERenderer ter = new TERenderer();
    private TETile[][] finalWorldFrame;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        ter.initialize(WIDTH, HEIGHT); //render
        Game game = new Game(WIDTH, HEIGHT);
        game.start(); // displays main menu

        // check if user want animated reload
        if (game.animatedReload() && !game.over()) {
            while (game.hasNextStep()) {
                game.replay();
                finalWorldFrame = game.getWorld();
                ter.renderFrame(finalWorldFrame); // draw the world to the screen
                game.refreshStats();
                StdDraw.show();
                StdDraw.pause(100);
            }
            game.purgeCharBuffer(); // cleans any key stroke pressed during replay
        }
        // main game loop
        while (!game.over()) {
            game.play();
            finalWorldFrame = game.getWorld();
            ter.renderFrame(finalWorldFrame); // draw the world to the screen
            game.refreshStats();
            StdDraw.show();
        }
        game.displayFinalMessage();
        System.out.println("Game ended!");
        //System.exit(0);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // check if given input is valid
        //ter.initialize(WIDTH, HEIGHT);
        ArgumentAnalyzer analyzer = new ArgumentAnalyzer(input);
        if (!analyzer.success()) {
            return null;
        }
        Game game = new Game(WIDTH, HEIGHT);
        game.executeArgument(analyzer);
        finalWorldFrame = game.getWorld();
        //ter.renderFrame(finalWorldFrame); // draw the world to the screen
        //StdDraw.show();
        return finalWorldFrame;
    }

    public TETile[][] interactWithInputStringOriginal(String input) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        ter.initialize(WIDTH, HEIGHT);

        // check if given input is valid
        ArgumentAnalyzer analyzer = new ArgumentAnalyzer(input);
        if (!analyzer.success()) {
            return null;
        }

        RandomGen rgen = new RandomGen(analyzer.getSeed()); // random number generator
        finalWorldFrame = new GridCreator(WIDTH, HEIGHT, rgen, false).grid(); // get world
        ter.renderFrame(finalWorldFrame); // draw the world to the screen
        return finalWorldFrame;
    }

    public String toString() {
        if (finalWorldFrame == null) {
            return "The gird is empty";
        }
        String grid = "";
        for (int i = finalWorldFrame[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < finalWorldFrame.length; j++) {
                grid += finalWorldFrame[j][i].toString();
            }
            grid += "\n"; // new line
        }

        return grid;
    }
}
