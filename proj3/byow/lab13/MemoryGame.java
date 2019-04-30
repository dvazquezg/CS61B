package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private static Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        int seed = Integer.parseInt(args[0]);
        rand = new Random(seed);
        MemoryGame game = new MemoryGame(40, 40);
        game.startGame();
    }

    public MemoryGame(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String str = "";
        for (int i = 0; i < n; i++) {
            str += CHARACTERS[rand.nextInt(CHARACTERS.length)];
        }
        return str;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        int x = width / 2;
        int y = height / 2;
        drawStatusBar();
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(x, y, s);
        StdDraw.show();
    }

    public void drawStatusBar() {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.rectangle(20, 39, 19.9, 1);
        int x = 3;
        int y = 39;
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(x, y, "Round: " + round);
        x = 20;
        if (!gameOver && playerTurn) {
            StdDraw.text(x, y, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        }
        if (!gameOver && !playerTurn) {
            StdDraw.text(x, y, "Watch then type!");
        }

    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        playerTurn = false;
        for (int i = 0; i < letters.length(); i++) {
            drawFrame("" + letters.charAt(i));
            StdDraw.pause(1000);
            StdDraw.clear(Color.BLACK);
            drawStatusBar();
            StdDraw.show();
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String str = "";

        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                str += StdDraw.nextKeyTyped();
                n -= 1;
            }
            System.out.println(str);
        }
        return str;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        String randomStr, userInput;
        round = 1;
        playerTurn = false;
        while (true) {
            drawFrame("Round: " + round);
            StdDraw.pause(1000);// show round for 1 sec
            randomStr = generateRandomString(round);
            flashSequence(randomStr); // show sequence 1 chat at a a time
            userInput = solicitNCharsInput(round);
            playerTurn = true;
            if (randomStr.equals(userInput)) {
                gameOver = false;
                round++;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
                break;
            }
        }
    }

}
