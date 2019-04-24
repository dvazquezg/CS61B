package byow.Core;

public class Tile implements Point {
    private int xpos;
    private int ypos;

    public Tile(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    @Override
    public int getXpos() {
        return this.xpos;
    }
    @Override
    public int getYpos() {
        return this.ypos;
    }
}
