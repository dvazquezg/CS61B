package byow.Core;

public class Door implements Point{
    private int xpos;
    private int ypos;

    public Door(int xpos, int ypos) {
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
