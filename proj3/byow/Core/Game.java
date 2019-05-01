package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
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


    public Game(int w, int h) {
        creatures = new HashSet<>();
        trace = new ArrayList<>();
        this.width = w;
        this.height = h;
    }

    public void start() {
        gameSequence = "";
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
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(x, y + 1.5, "Enter seed number followed by letter 'S':");
            // print flashing prompt
            if (decrease) {
                flashUnderscore--;
                StdDraw.text(x, y, gameSequence + "_");
                if (flashUnderscore == 0) {
                    decrease = false;
                }
            } else {
                flashUnderscore++;
                StdDraw.text(x, y, gameSequence + " ");
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
        y = height / 2 - 1.5;
        font = new Font("Monaco", Font.BOLD, 17);
        StdDraw.setFont(font);
        StdDraw.text(x, y, "New Game (N)");
        y = height / 2;
        StdDraw.text(x, y, "Load Game (L)");
        y = height / 2 + 1.5;
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
        //System.out.println("Current sequence: " + gameSequence);
        //System.out.println(trace);
        if (commandMode) {
            commandListener();
        } else {
            movePlayer(newMove);
        }
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
            System.out.println("Loading...");
            loadGame();
        }
        restoreState(input.getRawInput());
    }

    private void directStateRecovery() {
        while (analyzer.hasNextStep()) {
            replay();
        }
    }

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
        //StdDraw.pause(500);

        // check if the creature was moved
        if (!moveCreature(player, currentMove)) {
            // remove last move from trace and string
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
        return true; // creature was moved
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



    public void initialWorld(long seed) {
        RandomGen rgen = new RandomGen(seed); // random number generator
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
