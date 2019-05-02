package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import static byow.Core.Constants.Direction;
import static byow.Core.Constants.Action;

public class Game {
    private GridCreator worldGen;
    private TETile[][] world;
    private boolean gameOver = false;
    private int width;
    private int height;
    private Color titleColor = new Color(153, 223, 205);
    private Color dynamicColor = titleColor;
    private boolean darker = true;
    private boolean commandMode = false;
    private String gameSequence;
    private ArgumentAnalyzer analyzer;
    private Avatar player;
    private HashSet<Creature> creatures;
    private ArrayList<Direction> trace; // records players movements
    private String saveFile = "game.txt";
    private boolean animatedReload = false;
    private RandomGen rgen; // random gen
    private String currMouseTile;


    public Game(int w, int h) {
        creatures = new HashSet<>();
        trace = new ArrayList<>();
        this.width = w;
        this.height = h;
    }

    public void start() {
        gameSequence = "";
        currMouseTile = "Replay...";
        boolean invalidOption = false;
        Character option;
        while (true) {
            mainMenu(invalidOption); // display menu
            if (StdDraw.hasNextKeyTyped()) {
                option = StdDraw.nextKeyTyped();
                option = Character.toUpperCase(option);
                switch (option) {
                    case 'N':
                        startNewGame();
                        return;
                    case  'L':
                        loadGame();
                        return;
                    case  'R':
                        animatedReload = true;
                        loadGame();
                        return;
                    case  'Q':
                        gameOver = true;
                        System.exit(0);
                        return;
                    default:
                        invalidOption = true;
                        break;
                }
            }
        }
    }

    private void startNewGame() {
        Font font = new Font("Monaco", Font.BOLD, 17);
        StdDraw.setFont(font);
        double x = width / 2;
        double y = height / 2;
        gameSequence = "N";
        // loop until a valid input is entered
        int flashUnderscore = 100;
        boolean decrease = true;
        // prompt user for seed, loop until valid seed has been entered
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(x, y + 1.5, "Enter seed number followed by letter 'S':");
            // print flashing prompt
            String displaySequence = gameSequence.substring(1); // skip 'N'
            if (decrease) {
                flashUnderscore--;
                StdDraw.text(x, y, displaySequence + "_");
                if (flashUnderscore == 0) {
                    decrease = false;
                }
            } else {
                flashUnderscore++;
                StdDraw.text(x, y, displaySequence + " ");
                if (flashUnderscore == 100) {
                    decrease = true;
                }
            }
            StdDraw.show();

            // retrieves user input
            if (StdDraw.hasNextKeyTyped()) {
                char car = StdDraw.nextKeyTyped();
                car = Character.toUpperCase(car);
                gameSequence += car;
                if (car == 'S') {
                    // analyze input
                    analyzer = new ArgumentAnalyzer(gameSequence);
                    if (analyzer != null && analyzer.success()) {
                        initialWorld(analyzer.getSeed()); // set the grid
                        createMainPlayer(); // positions main player's avatar
                        break; // break loop and return to start() loop
                    } else {
                        gameSequence = "N"; // reset if sequence is invalid
                    }
                }
            }
        }
    }
    private void createMainPlayer() {
        SimplePoint playerLoc = worldGen.getRandAvaFloorLoc(); // get available location
        player = new Avatar(playerLoc, "You"); // create player
        placeCreatureOnWorld(player); // place main player in grid
    }

    private void placeCreatureOnWorld(Creature creature) {
        creatures.add(creature); // add to array of creatures
        world[creature.getX()][creature.getY()] = creature.getTile(); // add to grid
    }

    private void mainMenu(boolean invalidOption) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        double x = width / 2;
        double y = 3 * height / 4;
        // draw title
        setTitleColor();
        StdDraw.setPenColor(dynamicColor);
        StdDraw.text(x, y, "61B Dungeon Explorers: The Game");
        // draw options
        StdDraw.setPenColor(Color.WHITE);
        font = new Font("Monaco", Font.BOLD, 17);
        StdDraw.setFont(font);
        y = height / 2 + 3;
        StdDraw.text(x, y, "New Game (N)");
        y = height / 2 + 1.5;
        StdDraw.text(x, y, "Load Game (L)");
        y = height / 2;
        StdDraw.text(x, y, "Load & replay (R)");
        y = height / 2 - 1.5;
        StdDraw.text(x, y, "Quit Game (Q)");

        if (invalidOption && dynamicColor.getRed() < 75) {
            font = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.RED);
            y = height / 3;
            StdDraw.text(x, y, "Invalid Option, try again!");
        }
        StdDraw.show();
    }

    public void play() {
        boolean newMove = moveListener();
        if (commandMode) {
            commandListener();
        } else {
            movePlayer(newMove);
        }
        mouseListener(); // listen to mouse actions
    }

    private void mouseListener() {
        int xTile = (int) StdDraw.mouseX();
        int yTile = (int) StdDraw.mouseY();
        if (xTile < width && yTile < height) {
            currMouseTile = world[xTile][yTile].description();
        } else {
            currMouseTile = "nothing";
        }
    }

    public void refreshStats() {
        Font font = new Font("Monaco", Font.BOLD, 18);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setPenRadius(0.003);
        StdDraw.line(0, height + 0.07, width, height + 0.07); // top line
        double x = 4;
        double y = height + 1;
        StdDraw.text(x, y, currMouseTile); // display description of tile under pointer
        x = width - 7.5;
        StdDraw.text(x, y, getDate()); // display description of tile under pointer
    }

    private String getDate() {
        Date date = new Date(); // this object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    private void commandListener() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                key = Character.toUpperCase(key);
                switch (key) {
                    case 'Q':
                        saveGame();
                        return;
                    default:
                        commandMode = false;
                        return;
                }
            }
        }
    }

    private void saveGame() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));
            out.write(gameSequence); // game sequence
            out.close();
        } catch (IOException e) {
            System.out.println("Game was not saved!");
        }
        gameOver = true; // end the game (stops main loop if running)
    }

    private void loadGame() {
        String savedGameSequence = readFile();
        if (savedGameSequence != null) {
            restoreState(savedGameSequence);
        } else {
            gameOver = true;
            System.out.println("File is empty!");
        }
    }

    private String readFile() {
        String str = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(saveFile));
            str = reader.readLine();
            reader.close();
        } catch (IOException e) {
            System.out.println("File does not exist!");
            gameOver = true;
        }
        return str;
    }

    private void restoreState(String sequence) {
        analyzer = new ArgumentAnalyzer(sequence); // takes string sequence and analyzes it
        if (!analyzer.success()) {
            gameOver = true;
            System.out.println("Invalid or corrupted sequence");
            return;
        }

        if (analyzer.getAction() == Action.NEW) {
            // initialize game Sequence
            gameSequence = "N" + analyzer.getSeed() + "S";
            // initialize world
            initialWorld(analyzer.getSeed()); // set the grid
            createMainPlayer(); // positions main player's avatar
        }

        if (analyzer.hasSteps() && !animatedReload) {
            directStateRecovery(); // recovers state
        }

        // if loaded sequence contains save command, save
        if (analyzer.saveState()) {
            saveGame();
        }
    }

    /**
     * executes string command
     * @param input
     */
    public void executeArgument(ArgumentAnalyzer input) {
        if (input.getAction() == Action.LOAD) {
            loadGame();
        }
        restoreState(input.getRawInput());
    }

    /**
     * Recovers saved state without displaying anything
     */
    private void directStateRecovery() {
        while (analyzer.hasNextStep()) {
            replay();
        }
    }

    /**
     * similar to play, but key strokes are taken from Argument analyzer
     * which is initialized from the string stored in file or passed argument
     */
    public void replay() {
        Step currentStep = analyzer.getNextStep();
        gameSequence += currentStep.getCharStep();
        trace.add(currentStep.getDirStep());
        movePlayer(true);
    }

    public void movePlayer(boolean move) {
        if (trace.size() == 0 || !move) {
            return;
        }

        Direction currentMove = trace.get(trace.size() - 1); // get last move

        // check if the creature was moved
        if (!moveCreature(player, currentMove)) {
            // if not, remove last move from trace and string
            trace.remove(trace.size() - 1);
            gameSequence = gameSequence.substring(0, gameSequence.length() - 1);
        }
    }

    private boolean validCreatureMove(SimplePoint newPosition) {
        TETile destination = world[newPosition.getXpos()][newPosition.getYpos()];
        if (!destination.equals(Constants.FLOORTILE)) {
            return false;
        }
        return true;
    }

    private boolean moveCreature(Creature creature, Direction dir) {
        SimplePoint oldLocation = new SimplePoint(creature.getX(), creature.getY());
        creatures.remove(creature);
        switch (dir) {
            case NORTH:
                creature.moveNorth();
                break;
            case WEST:
                creature.moveWest();
                break;
            case SOUTH:
                creature.moveSouth();
                break;
            case EAST:
                creature.moveEast();
                break;
            default:
                break;
        }

        if (!validCreatureMove(creature.getLocation())) {
            creature.setLocation(oldLocation);
            creatures.add(creature);
            return false;
        }

        creatures.add(creature); // add updated creature
        world[oldLocation.getXpos()][oldLocation.getYpos()] = Constants.FLOORTILE;
        world[creature.getX()][creature.getY()] = creature.getTile();
        explore(creature.getLocation()); // cool line of sight effect
        return true; // creature was moved
    }

    private void explore(SimplePoint position) {
        int x = position.getXpos();
        int y = position.getYpos();
        //System.out.println("x: " + x + " y: " + y);
        for (int radius = 1; radius <= Constants.EXPLORERADIUS; radius++) {
            makeLighter(x, y, radius);
        }
    }

    private void makeLighter(int x, int y, int radius) {
        for (int row = x - radius; row <= x + radius; row++) {
            for (int col = y - radius; col <= y + radius; col++) {
                if (row >= 0 && row < width && col >= 0 && col < height) {

                    if (row == x && col == y) {
                        continue;
                    }
                    world[row][col] = TETile.lighterTile(world[row][col], radius);
                }
            }
        }
    }


    private boolean moveListener() {
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            key = Character.toUpperCase(key);
            switch (key) {
                case 'W':
                    gameSequence += 'W';
                    trace.add(Direction.NORTH);
                    return true;
                case 'A':
                    gameSequence += 'A';
                    trace.add(Direction.WEST);
                    return true;
                case 'S':
                    gameSequence += 'S';
                    trace.add(Direction.SOUTH);
                    return true;
                case 'D':
                    gameSequence += 'D';
                    trace.add(Direction.EAST);
                    return true;
                case ':':
                    commandMode = true; // activate command mode
                    return false;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Generated initial grid
     * @param seed seed number to seed random generator
     */
    public void initialWorld(long seed) {
        rgen = new RandomGen(seed); // random number generator
        worldGen = new GridCreator(width, height, rgen); // get world grid
        world = worldGen.grid();
    }

    private void setTitleColor() {
        int r = dynamicColor.getRed();
        int g = dynamicColor.getGreen();
        int b = dynamicColor.getBlue();

        if (darker) {
            r = r - 1;
            g = g - 1;
            b = b - 1;

            if (r == 0 || g == 0 || b == 0) {
                darker = false;
            }
        } else {
            r = r + 1;
            g = g + 1;
            b = b + 1;

            if (r == titleColor.getRed() || g == titleColor.getGreen()
                    || b == titleColor.getBlue()) {
                darker = true;
            }
        }
        dynamicColor = new Color(r, g, b);
    }

    public boolean over() {
        return gameOver;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public boolean animatedReload() {
        return animatedReload;
    }

    public boolean hasNextStep() {
        return analyzer.hasNextStep();
    }

    public void purgeCharBuffer() {
        // clean accidental key presses before the game starts
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }
}
