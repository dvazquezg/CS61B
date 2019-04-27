package byow.Core;

import byow.TileEngine.TETile;
import java.util.ArrayList;
import static byow.Core.Constants.*;
import static byow.Core.GridCreator.isOutOfBounds;
import static byow.Core.GridCreator.overlaps;

public class Hallway implements InteriorSpace {
    protected int xlowl;      // The x coordinate of the lower left tile of the hallway.
    protected int ylowl;      // The y coordinate of the lower left tile of the hallway.
    protected int xupr;       // The x coordinate of the upper right tile of the hallway.
    protected int yupr;       // The y coordinate of the upper right tile of the hallway.
    protected int length;     // The hallway's length
    protected Direction dir;  // The hallways's direction
    protected ArrayList<Door> doors;      // The hallways's doors
    protected TETile wallTile = WALLTILE; // The hallways's wall tile
    protected TETile doorTile = DOORTILE; // The default door tile type
    protected boolean created = false;
    protected Door endDoor;

    public Hallway(Room room, RandomGen rgen) {
        doors = new ArrayList<>();
        makeHallway(room, rgen);
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

    private void makeHallway(Room room, RandomGen rgen) {
        Door door = room.getRandomAvailableDoor(rgen); // get a random door from given room
        this.dir = door.getDir();
        switch (dir) {
            case NORTH:
                northHallway(door, room, rgen);
                break;
            case WEST:
                westHallway(door, room, rgen);
                break;
            case SOUTH:
                southHallway(door, room, rgen);
                break;
            case EAST:
                eastHallway(door, room, rgen);
                break;
            default:
                break;
        }
    }

    private void northHallway(Door door, Room room, RandomGen rgen) {
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
        for (Room troom : GridCreator.rooms) {
            while (overlaps(troom, dummyHallway)) {
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
        if (this.yupr > door.getYpos()) {
            // make door on the other sie
            Door newStartDoor = new Door(door.getXpos(), door.getYpos() + 1,
                    doorTile, getOpositeDir(dir));
            newStartDoor.connect(); // carve door from hallway
            door.connect(); // carve door from room
            Door newEndDoor = new Door(door.getXpos(), door.getYpos() + length,
                    doorTile, dir); // create door

            // if the corridor is just of length 1, then star & end door are in same pos
            if (!newStartDoor.samePos(newEndDoor)) {
                this.doors.add(newStartDoor); // we only add start door if hallway.length > 1
            }
            this.doors.add(newEndDoor);
            endDoor = newEndDoor;
            created = true;
        } else {
            room.doors.remove(door); // remove northern door of this room
        }

    }

    private void southHallway(Door door,  Room room, RandomGen rgen) {
        length = rgen.random(MIN_HALLWAY_LEN, MAX_HALLWAY_LEN);
        xlowl = door.getXpos() - 1;
        ylowl = door.getYpos() - length;
        xupr = door.getXpos() + 1;
        yupr = door.getYpos() - 1;
        Hallway dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);

        while (isOutOfBounds(dummyHallway)) {
            length -= 1; // decrease size
            ylowl += 1;
            dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
        }

        // check if overlaps with other rooms
        for (Room troom : GridCreator.rooms) {
            while (overlaps(troom, dummyHallway)) {
                length -= 1; // decrease size
                ylowl += 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if overlaps with other hallways
        for (Hallway hallway : GridCreator.hallways) {
            while (overlaps(hallway, dummyHallway)) {
                length -= 1; // decrease size
                ylowl += 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if hallway ylowl is the same as door.ylowl (meaning is does not have space)
        if (this.ylowl < door.getYpos()) {
            // make door on the other sie
            Door newStartDoor = new Door(door.getXpos(), door.getYpos() - 1,
                    doorTile, getOpositeDir(dir));
            newStartDoor.connect(); // carve door from hallway
            door.connect(); // carve door from room
            Door newEndDoor = new Door(door.getXpos(), door.getYpos() - length,
                    doorTile, dir); // create door

            // if the corridor is just of length 1, then star & end door are in same pos
            if (!newStartDoor.samePos(newEndDoor)) {
                this.doors.add(newStartDoor); // we only add start door if hallway.length > 1
            }
            this.doors.add(newEndDoor);
            endDoor = newEndDoor;
            created = true;
        } else {
            room.doors.remove(door); // remove northern door of this room
        }

    }

    private void westHallway(Door door, Room room, RandomGen rgen) {
        length = rgen.random(MIN_HALLWAY_LEN, MAX_HALLWAY_LEN);
        xlowl = door.getXpos() - length;
        ylowl = door.getYpos() - 1;
        xupr = door.getXpos() - 1;
        yupr = door.getYpos() + 1;
        Hallway dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);

        while (isOutOfBounds(dummyHallway)) {
            length -= 1; // decrease size
            xlowl += 1;
            dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
        }

        // check if overlaps with other rooms
        for (Room troom : GridCreator.rooms) {
            while (overlaps(troom, dummyHallway)) {
                length -= 1; // decrease size
                xlowl += 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if overlaps with other hallways
        for (Hallway hallway : GridCreator.hallways) {
            while (overlaps(hallway, dummyHallway)) {
                length -= 1; // decrease size
                xlowl += 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        if (this.xlowl < door.getXpos()) {
            // make door on the other sie
            Door newStartDoor = new Door(door.getXpos() - 1, door.getYpos(),
                    doorTile, getOpositeDir(dir));
            newStartDoor.connect(); // carve door from hallway
            door.connect(); // carve door from room
            Door newEndDoor = new Door(door.getXpos() - length, door.getYpos(),
                    doorTile, dir); // create door
            if (!newStartDoor.samePos(newEndDoor)) {
                this.doors.add(newStartDoor);
            }
            this.doors.add(newEndDoor);
            endDoor = newEndDoor;
            created = true;
        } else {
            room.doors.remove(door); // remove northern door of this room
        }

    }

    private void eastHallway(Door door,  Room room, RandomGen rgen) {
        length = rgen.random(MIN_HALLWAY_LEN, MAX_HALLWAY_LEN);
        xlowl = door.getXpos() + 1;
        ylowl = door.getYpos() - 1;
        xupr = door.getXpos() + length;
        yupr = door.getYpos() + 1;
        Hallway dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);

        while (isOutOfBounds(dummyHallway)) {
            length -= 1; // decrease size
            xupr -= 1;
            dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
        }

        // check if overlaps with other rooms
        for (Room troom : GridCreator.rooms) {
            while (overlaps(troom, dummyHallway)) {
                length -= 1; // decrease size
                xupr -= 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        // check if overlaps with other hallways
        for (Hallway hallway : GridCreator.hallways) {
            while (overlaps(hallway, dummyHallway)) {
                length -= 1; // decrease size
                xupr -= 1;
                dummyHallway = new Hallway(xlowl, ylowl, xupr, yupr);
            }
        }

        if (this.xupr > door.getXpos()) {
            // make door on the other sie
            Door newStartDoor = new Door(door.getXpos() + 1, door.getYpos(),
                    doorTile, getOpositeDir(dir));
            newStartDoor.connect(); // carve door from hallway
            door.connect(); // carve door from room
            Door newEndDoor = new Door(door.getXpos() + length, door.getYpos(),
                    doorTile, dir); // create door
            if (!newStartDoor.samePos(newEndDoor)) {
                this.doors.add(newStartDoor);
            }
            this.doors.add(newEndDoor);
            endDoor = newEndDoor;
            created = true;
        } else {
            room.doors.remove(door); // remove northern door of this room
        }

    }

    private Direction getOpositeDir(Direction orgdir) {
        switch (orgdir) {
            case NORTH:
                return Direction.SOUTH;
            case WEST:
                return Direction.EAST;
            case SOUTH:
                return Direction.NORTH;
            case EAST:
                return Direction.WEST;
            default:
                break;
        }
        return null;
    }

    public String toString() {
        String info = "Hallway length " + length + ", ";
        info += "lower left: (" + xlowl + ", " + ylowl + ") ";
        info += "upper right: (" + xupr + ", " + yupr + ")";
        return info;
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

    public boolean wasCreated() {
        return created;
    }

    public Door getEndDoor() {
        return endDoor;
    }

    public TETile getWallTile() {
        return wallTile;
    }

}
