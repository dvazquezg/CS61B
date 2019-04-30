package byow.Core;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

/**
 * Provides user interface utilities
 * It assumes that an instance of StdDraw.setCanvasSize exist
 */
public class UIRenderer {
    private int width;
    private int height;

    public void initialize(int w, int h) {
        this.width = w;
        this.height = h;
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.show();
    }

    public void mainMenu() {
        Font font = new Font("Monaco", Font.BOLD, 24);
        StdDraw.setFont(font);
        int x = width / 2;
        int y = height / 2;
        //drawStatusBar();
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(x, y, "CS61B: THE GAME");
        StdDraw.show();
        StdDraw.pause(2000);

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

    public void statusBar() {
        //Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        //StdDraw.setFont(font);

    }
}
