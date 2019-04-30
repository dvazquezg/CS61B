package byow.Core;

public class SimplePoint implements Point {

    private int x;
    private int y;

    public SimplePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getXpos() {
        return x;
    }

    @Override
    public int getYpos() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
