package byow.Core;

import byow.Core.Constants.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.HashSet;
import static byow.Core.Constants.*;

public class Room implements InteriorSpace{
    protected int xlowl;                      // The x coordinate of the lower left tile of the room.
    protected int ylowl;                      // The y coordinate of the lower left tile of the room.
    protected int xupr;                       // The x coordinate of the upper right tile of the room.
    protected int yupr;                       // The y coordinate of the upper right tile of the room.
    protected int roomWidth;                  // The room's width in tiles
    protected int roomHeight;                 // The room's height in tiles
    protected TETile roomWall;                // The room's wall tile
    protected TETile doorTile = Tileset.SAND; // The default door tile type
    protected ArrayList<Door> doors;          // Array of rooms


    /**
     * Creates the first room of the world approximately in the center
     * @param columns grid width
     * @param rows grid height
     * @param roomWall tile for room's wall
     * @param rgen random generator
     */
    public Room(int columns, int rows, TETile roomWall, RandomGen rgen) {
        this.roomWidth = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        this.roomHeight = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        this.roomWall = roomWall;
        doors = new ArrayList<>();
        xlowl = (int) (columns / 2f - roomWidth / 2f);
        ylowl = (int) (rows / 2f - roomHeight / 2f);
        xupr = xlowl + roomWidth - 1;
        yupr = ylowl + roomHeight - 1;
        setDoors(rgen, null); // null since there is no entering hallway
    }

    /**
     * Creates a room from entering hallway
     * @param columns grid width
     * @param rows grid height
     * @param roomWall tile for room's wall
     * @param hallway the entering hallway
     * @param rgen random generator
     */
    public Room(int columns, int rows, TETile roomWall, Hallway hallway, RandomGen rgen) {
        this.roomWidth = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);//<<<< chenge
        this.roomHeight = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        this.roomWall = roomWall;
        doors = new ArrayList<>();
        xlowl = (int) (columns / 2f - roomWidth / 2f);
        ylowl = (int) (rows / 2f - roomHeight / 2f);
        xupr = xlowl + roomWidth - 1;
        yupr = ylowl + roomHeight - 1;

        //setDoors(rgen, hallway.dir);
    }

    private void setDoors(RandomGen rgen, Direction enteringHallDir){
        int ndoors;
        HashSet<Direction> usedDirs = new HashSet<>(); // hold used directions
        // check if entering direction from hallway is provided
        if (enteringHallDir == null) {
            ndoors = rgen.random(1, 4); // can use 4 sides
        } else {
            // if a hallways in entering this
            ndoors = rgen.random(1, 3); // can only use 3 sides
            usedDirs.add(enteringHallDir); // add entering dir to used set
        }
        System.out.println("number of doors: " + ndoors);

        int indexDir = rgen.random(0, 3); // random starting index
        // create ndoors
        while (ndoors != 0) {
            // loop until get a random unused direction is found
            Direction currDir = Constants.Direction.values()[indexDir];
            if (!usedDirs.contains(currDir)) {
                Door newDoor = makeDoor(currDir, rgen);
                doors.add(newDoor);
                usedDirs.add(currDir); // add direction to temp usedDirs
                ndoors -= 1;
            }
            indexDir  = (indexDir  + 1) % 4; // move to the next direction
        }
    }

    /**
     * Generates a door ar the specified at he indicated location
     * @param dir the Direction where the door must be placed
     * @param rgen the door
     */
    private Door makeDoor(Direction dir, RandomGen rgen) {
        Door newDoor = null;
        int xpos = -1, ypos = -1;
        switch (dir) {
            case NORTH:
                xpos = rgen.random(xlowl + 1, xupr - 1); // x-pos on top room rim
                ypos = yupr; // y-pos is top rim coordinate
                break;
            case WEST:
                ypos = rgen.random(ylowl + 1, yupr - 1); // x-pos on top room rim
                xpos = xlowl; // x-pos is left rim coordinate
                break;
            case SOUTH:
                xpos = rgen.random(xlowl + 1, xupr - 1); // x-pos on bottom room rim
                ypos = ylowl; // y-pos is bottom rim coordinate
                break;
            case EAST:
                ypos = rgen.random(ylowl + 1, yupr - 1); // x-pos on top room rim
                xpos = xupr; // x-pos is left rim coordinate
                break;
        }
        newDoor = new Door(xpos, ypos, doorTile, dir); // create door
        return newDoor;
    }

    /**
     * creates room at determined position
     * for debugging purposes only
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

    public String toString(){
        String info = "Room size " + roomWidth + "w x " + roomHeight + "h || ";
        info += "lower left: (" + xlowl + ", "+ ylowl + ") ";
        info += "upper right: (" + xupr + ", "+ yupr + ")";
        return info;
    }

    /**
     * Returns if the room has available doors
     * @return
     */
    public boolean hasAvailableDoors(){
        for (Door door : doors) {
            if (!door.isConnected()) {
                return true;
            }
        }
        return false;
    }

    //
    public Door getRandomAvailableDoor(RandomGen rgen) {
        if (doors.size() == 0) {
            return null;
        }
        int randomIndex = rgen.random(0, doors.size() -1);
        for (int i = 0; i < doors.size(); i++) {
            Door currentDoor = doors.get(randomIndex);
            if (!currentDoor.isConnected()) {
                return currentDoor;
            }
            // get next door
            randomIndex = (randomIndex + 1) % doors.size();
        }
        return null;
    }

    @Override
    public int getxlowl() {
        return xlowl;
    }

    @Override
    public int getylowl() {
        return ylowl;
    }

    @Override
    public int getxupr() {
        return xupr;
    }

    @Override
    public int getyupr() {
        return yupr;
    }
}
