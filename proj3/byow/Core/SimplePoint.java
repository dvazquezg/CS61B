package byow.Core;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePoint that = (SimplePoint) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point id: %d (x: %d, y: %d)", id, x, y);
    }
}
