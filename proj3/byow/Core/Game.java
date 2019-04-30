package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

public class Game {
    private TETile[][] world;
    private boolean gameOver = false;
    private int width;
    private int height;
    private Color titleColor = new Color(153, 223, 205);
    private Color dynamicColor = titleColor;
    private boolean darker = true;
    private String gameSequence;
    private ArgumentAnalyzer analyzer;

    public Game(int w, int h) {
        this.width = w;
        this.height = h;
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.show();
    }

    public void start() {
        gameSequence = "";
        boolean invalidOption = false;
        Character option;
        while (true) {
            mainMenu(invalidOption);
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
        while(true){
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(x, y + 1.5, "Enter seed number followed by letter 'S':");

            if (decrease) {
                flashUnderscore--;
                StdDraw.text(x, y, gameSequence + "_");
                if (flashUnderscore == 0){
                    decrease = false;
                }
            } else {
                flashUnderscore++;
                StdDraw.text(x, y, gameSequence + " ");
                if (flashUnderscore == 100){
                    decrease = true;
                }
            }
            StdDraw.show();

            if (StdDraw.hasNextKeyTyped()) {
                char car = StdDraw.nextKeyTyped();
                car = Character.toUpperCase(car);
                gameSequence += car;
                if (car == 'S') {
                    analyzer = new ArgumentAnalyzer(gameSequence);
                    if (analyzer != null && analyzer.success()) {
                        initialWorld(analyzer.getSeed()); // set the grid

                        break;// break loop and return to start() loop
                    } else {
                        gameSequence = "N";
                    }
                }
            }
        }
    }

    private void loadGame() {

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
        //StdDraw.pause(2000);
        //initialWorld(122343);
    }

    public void play() {
        ArgumentAnalyzer analyzer = new ArgumentAnalyzer("n5197880843569031643s");
        if (!analyzer.success()) {
            // Show error message
        }
    }

    public void getSeed() {
        ArgumentAnalyzer analyzer = new ArgumentAnalyzer("n5197880843569031643s");
        if (!analyzer.success()) {
            // Show error message
        }
    }



    public void initialWorld(long seed) {
        RandomGen rgen = new RandomGen(seed); // random number generator
        world = new GridCreator(width, height, rgen).grid(); // get world grid
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
        } else{
            r = r + 1;
            g = g + 1;
            b = b + 1;

            if (r == titleColor.getRed() || g == titleColor.getGreen() || b == titleColor.getBlue()) {
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

}
