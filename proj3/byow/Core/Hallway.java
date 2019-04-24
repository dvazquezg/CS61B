package byow.Core;

import byow.Core.Constants.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import static byow.Core.Constants.*;
import static byow.Core.GridCreator.isOutOfBounds;
import static byow.Core.GridCreator.overlaps;

public class Hallway implements InteriorSpace{
    protected int xlowl;                 // The x coordinate of the lower left tile of the hallway.
    protected int ylowl;                 // The y coordinate of the lower left tile of the hallway.
    protected int xupr;                  // The x coordinate of the upper right tile of the hallway.
    protected int yupr;                  // The y coordinate of the upper right tile of the hallway.
    protected int length;                // The hallway's length
    protected Direction dir;             // The hallways's direction
    protected ArrayList<Door> doors;     // The hallways's doors
    protected TETile hallwayWall;        // The hallways's wall tile
    protected TETile doorTile = Tileset.SAND; // The default door tile type

    public Hallway(int columns, int rows, TETile hallwayWall, Room room, RandomGen rgen) {
        Door door = room.getRandomAvailableDoor(rgen);
        doors = new ArrayList<>();
        this.hallwayWall = hallwayWall;
        makeHallway(columns, rows, door, rgen);
    }

    /**
     * creates dummy hallways to help checking overlaps
     * @param xlowl
     * @param ylowl
     * @param xupr
     * @param yupr
     */
    public Hallway(int xlowl, int ylowl, int xupr, int yupr) {
        this.xlowl = xlowl;
        this.ylowl = ylowl;
        this.xupr = xupr;
        this.yupr = yupr;
    }

    private void makeHallway(int columns, int rows, Door door, RandomGen rgen) {
        this.dir = door.getDir();
        System.out.println("Door choosed: " + dir);
        switch (dir) {
            case NORTH:

                break;
            case WEST:
                westHallway(columns, rows, door, rgen);
                break;
            case SOUTH:

                break;
            case EAST:

                break;
        }
    }

    private void northHallway(int columns, int rows, Door door, RandomGen rgen) {
        length = rgen.random(MIN_HALLWAY_LEN, MAX_HALLWAY_LEN);

        xlowl = door.getXpos() - 1;
        ylowl = door.getYpos() + 1;
        xupr = door.getXpos() + 1;
        yupr = door.getYpos() + length;
        Hallway dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);

        while (isOutOfBounds(dummyHallway)) {
            length -= 1; // decrease size
            yupr -= 1;
            dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
        }

        // check if overlaps with other rooms
        for (Room room : GridCreator.rooms) {
            while (overlaps(room, dummyHallway)) {
                length -= 1; // decrease size
                yupr -= 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if overlaps with other hallways
        for (Hallway hallway : GridCreator.hallways) {
            while (overlaps(hallway, dummyHallway)) {
                length -= 1; // decrease size
                yupr -= 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if hallway ylowl is the same as door.ylowl (meaning is does not have space)
        if (this.ylowl > door.getYpos()) {
            // make door on the other sie
            Door newDoor = new Door(door.getXpos(), door.getYpos() + length, doorTile, dir); // create door
            this.doors.add(newDoor);
            door.connect(); // carve door from room
        }

    }

    private void westHallway(int columns, int rows, Door door, RandomGen rgen) {
        length = rgen.random(MIN_HALLWAY_LEN, MAX_HALLWAY_LEN);
        xlowl = door.getXpos() - length;
        ylowl = door.getYpos() - 1;
        xupr = door.getXpos() - 1;
        yupr = door.getYpos() + 1;
        Hallway dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);

        while (isOutOfBounds(dummyHallway)) {
            length -= 1; // decrease size
            xlowl -= 1;
            dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
        }

        // check if overlaps with other rooms
        for (Room room : GridCreator.rooms) {
            while (overlaps(room, dummyHallway)) {
                length -= 1; // decrease size
                xlowl -= 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if overlaps with other hallways
        for (Hallway hallway : GridCreator.hallways) {
            while (overlaps(hallway, dummyHallway)) {
                length -= 1; // decrease size
                xlowl -= 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if hallway ylowl is the same as door.ylowl (meaning is does not have space)
        if (this.xlowl < door.getXpos()) {
            // make door on the other sie
            Door newStartDoor = new Door(door.getXpos() - 1, door.getYpos(), doorTile, dir);
            newStartDoor.connect(); // carve door from hallway
            door.connect(); // carve door from room
            this.doors.add(newStartDoor);
            Door newEndDoor = new Door(door.getXpos() - length, door.getYpos(), doorTile, dir); // create door
            this.doors.add(newEndDoor);
        }

    }

    private int getMaxExpansionLength(int columns, int rows, Door door, RandomGen rgen) {

        //while ()
        return 0;
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
