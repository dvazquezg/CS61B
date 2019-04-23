package byow.Core;

import byow.Core.Constants.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;

public class Room {
    protected int xlowl;                    // The x coordinate of the lower left tile of the room.
    protected int ylowl;                    // The y coordinate of the lower left tile of the room.
    protected int xupr;                     // The x coordinate of the upper right tile of the room.
    protected int yupr;                     // The y coordinate of the upper right tile of the room.
    protected int roomWidth;               // The room's width in tiles
    protected int roomHeight;              // The room's height in tiles
    protected Direction enterHallway;      // The direction of the corridor that is entering this room.
    protected TETile roomWall;
    protected HashSet<Door> doors;

    /**
     * Creates the first room of the world
     * @param columns
     * @param rows
     * @param roomWall
     * @param rgen
     */
    public Room(int columns, int rows, TETile roomWall, RandomGen rgen) {
        this.roomWidth = rgen.random(4, 12);
        this.roomHeight = rgen.random(4, 12);
        this.roomWall = roomWall;
        doors = new HashSet<>();
        xlowl = (int) (columns / 2f - roomWidth / 2f);
        ylowl = (int) (rows / 2f - roomHeight / 2f);
        xupr = xlowl + roomWidth - 1;
        yupr = ylowl + roomHeight - 1;
    }

    /**
     * creates room at determined position
     * @param xlowl
     * @param ylowl
     * @param roomWidth
     * @param roomHeight
     */
    public Room(int xlowl, int ylowl, int roomWidth, int roomHeight) {
        this.xlowl = xlowl;
        this.ylowl = ylowl;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        xupr = xlowl + roomWidth - 1;
        yupr = ylowl + roomHeight - 1;
    }

    public boolean overlaps(Room other){
        // Exclude parts where they do not overlap
        // If one rectangle is on left side of other
        if (other.xlowl > this.xupr || this.xlowl > other.xupr) {
            return false;
        }

        // If one rectangle is above other
        if (other.ylowl > this.yupr || this.ylowl > other.yupr) {
            return false;
        }

        return true;
    }

    public String toString(){
        String info = "Room size " + roomWidth + "w x " + roomHeight + "h || ";
        info += "lower left: (" + xlowl + ", "+ ylowl + ") ";
        info += "upper right: (" + xupr + ", "+ yupr + ")";
        return info;
    }

}
