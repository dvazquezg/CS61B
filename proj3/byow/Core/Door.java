package byow.Core;

import byow.Core.Constants.Direction;
import byow.TileEngine.TETile;

public class Door implements Point {
    private int xpos;
    private int ypos;
    private Direction dir;
    private boolean connected;
    private TETile tile;

    public Door(int xpos, int ypos, TETile tile, Direction dir) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.dir = dir;
        this.tile = tile;
        this.connected = false;

    }

    @Override
    public int getXpos() {
        return this.xpos;
    }

    @Override
    public int getYpos() {
        return this.ypos;
    }

    public Direction getDir() {
        return this.dir;
    }

    public void connect() {
        this.connected = true;
    }

    public boolean isConnected() {
        return connected;
    }

    public TETile getTile() {
        return tile;
    }

    public void setTile(TETile tile) {
        this.tile = tile;
    }

    public String toString() {
        String info = "Position: (" + xpos + ", " + ypos + ") ";
        info += "Connected?: " + connected + ", ";
        info += "Direction: " + dir + ", ";
        info += "Tile: " + tile;
        return info;
    }

    public boolean samePos(Door other) {
        return this.xpos == other.getXpos() && this.ypos == other.getYpos();
    }
}
