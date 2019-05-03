package byow.Core;

public class SimplePoint implements Point {

    private int x;
    private int y;
    private int id;

    public SimplePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SimplePoint(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    @Override
    public int getXpos() {
        return x;
    }

    @Override
    public int getYpos() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        SimplePoint otherPoint = (SimplePoint) other;
        return this.id == otherPoint.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("Point id: %d (x: %d, y: %d)", id, x, y);
    }
}
