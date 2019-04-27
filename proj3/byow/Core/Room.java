package byow.Core;

import byow.Core.Constants.*;
import byow.TileEngine.TETile;
import java.util.ArrayList;
import java.util.HashSet;
import static byow.Core.Constants.*;
import static byow.Core.GridCreator.isOutOfBounds;
import static byow.Core.GridCreator.overlaps;

public class Room implements InteriorSpace{
    protected int xlowl;                      // The x coordinate of the lower left tile of the room.
    protected int ylowl;                      // The y coordinate of the lower left tile of the room.
    protected int xupr;                       // The x coordinate of the upper right tile of the room.
    protected int yupr;                       // The y coordinate of the upper right tile of the room.
    protected int roomWidth;                  // The room's width in tiles
    protected int roomHeight;                 // The room's height in tiles
    protected int xposd;                      // The x coordinate of connecting door to incoming hallway
    protected int yposd;                      // The y coordinate of connecting door to incoming hallway
    protected TETile wallTile = WALLTILE;     // The room's wall tile
    protected TETile doorTile = DOORTILE;     // The default door tile type
    protected ArrayList<Door> doors;          // Array of rooms
    protected boolean created = false;        // Flag to indicate if room was created or not

    /**
     * Creates the first room of the world approximately in the center
     * @param columns grid width
     * @param rows grid height
     * @param rgen random generator
     */
    public Room(int columns, int rows, RandomGen rgen) {
        this.roomWidth = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        this.roomHeight = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        doors = new ArrayList<>();
        // place first room in the middle of the grid
        xlowl = (int) (columns / 2f - roomWidth / 2f); // W-36 E+36 || -29
        ylowl = (int) (rows / 2f - roomHeight / 2f);// N+12 S-10
        xupr = xlowl + roomWidth - 1;
        yupr = ylowl + roomHeight - 1;
        setDoors(rgen, null); // null since there is no entering hallway
        created = true;
    }

    /**
     * Creates a room from entering incomingHallway
     * @param incomingHallway the entering incomingHallway
     * @param rgen random generator
     */
    public Room(Hallway incomingHallway, RandomGen rgen) {
        doors = new ArrayList<>();
        makeRoom(incomingHallway, rgen);
    }

    private void makeRoom(Hallway hallway, RandomGen rgen){
        // set desire room size
        int desiredWidth = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        int desiredHeight = rgen.random(MIN_ROOM_SIDE, MAX_ROOM_SIDE);
        Door connectingDoor = makeEnteringDoor(hallway);

        // make dummy room of the smallest size possible & init dim instance variables
        setSmallestSize(connectingDoor);

        // try to reach desire size
        while(!desiredSize(desiredWidth, desiredHeight)) {
            if (!increaseSizeByOne(desiredWidth, desiredHeight, connectingDoor, rgen)) {
                break;
            }
        }

        // check if resulting room size is valid
        if (validRoomSize()) {
            // create doors for this room
            setDoors(rgen, connectingDoor);
            hallway.getEndDoor().connect();
            connectingDoor.connect();
            created = true;
        } else {
            // remove incoming door from hallway since room cannot be created here
            hallway.doors.remove(hallway.getEndDoor());
            created = false;
        }
    }

    private boolean increaseSizeByOne(int desiredWidth, int desiredHeight, Door connectingDoor, RandomGen rgen) {
        // increase size by one as long as room dims does not exceed target dims
        if (roomWidth < desiredWidth && increaseWidthByOne(connectingDoor, rgen)) {
            return true;
        }
        if (roomHeight < desiredHeight && increaseHeightByOne(connectingDoor, rgen)) {
            return true;
        }
        return false;
    }

    private boolean increaseWidthByOne(Door connectingDoor, RandomGen rgen) {
        // back up current size and corner coordinates
        SizeBackup bestSize = new SizeBackup(xlowl, ylowl, xupr, yupr, roomWidth, roomHeight);
        //boolean resized = false;
        Direction doorDir = connectingDoor.getDir();
        // increase width according new room's door placement

        if (doorDir == Direction.WEST) {
            // if connecting door faces West, extend eastward
            xupr += 1;
            roomWidth += 1;
            if (!roomFits(this)) {
                restoreBestSize(bestSize);
                return false;
            }
            return true; // room was resized successfully
        } else if (doorDir == Direction.EAST) {
            // if connecting door faces East, extend westward
            xlowl -= 1;
            roomWidth += 1;
            if (!roomFits(this)) {
                restoreBestSize(bestSize);
                return false;
            }
            return true; // room was resized successfully
        } else {
            // if connecting door faces North/South
            int randomSide = rgen.random(0, 1); // choose random side to begin with
            boolean extendWestward = (randomSide == 0);
            // loop two times
            for(int attempt = 1; attempt <= 2; attempt++) {
                if (extendWestward) {
                    // extend westward
                    xupr += 1;
                    extendWestward = false;
                } else {
                    // extend eastward
                    xlowl -= 1;
                    extendWestward = true;
                }
                roomWidth += 1;

                if (roomFits(this)) {
                    return true;
                }
                restoreBestSize(bestSize);
            }
            return false;
        }
    }

    private boolean increaseHeightByOne(Door connectingDoor, RandomGen rgen) {
        // back up current size and corner coordinates
        SizeBackup bestSize = new SizeBackup(xlowl, ylowl, xupr, yupr, roomWidth, roomHeight);
        //boolean resized = false;
        Direction doorDir = connectingDoor.getDir();
        // increase width according new room's door placement

        if (doorDir == Direction.NORTH) {
            // if connecting door faces North, extend southward
            ylowl -= 1;
            roomHeight += 1;
            if (!roomFits(this)) {
                restoreBestSize(bestSize);
                return false;
            }
            return true; // room was resized successfully
        } else if (doorDir == Direction.SOUTH) {
            // if connecting door faces South, extend northward
            yupr += 1;
            roomHeight += 1;
            if (!roomFits(this)) {
                restoreBestSize(bestSize);
                return false;
            }
            return true; // room was resized successfully
        } else {
            // if connecting door faces West/East
            int randomSide = rgen.random(0, 1); // choose random side to begin with
            boolean extendNorthward = (randomSide == 0);
            // loop two times
            for(int attempt = 1; attempt <= 2; attempt++) {
                if (extendNorthward) {
                    // extend northward
                    yupr += 1;
                    extendNorthward = false;
                } else {
                    // extend southward
                    ylowl -= 1;
                    extendNorthward = true;
                }
                roomHeight += 1;

                if (roomFits(this)) {
                    return true;
                }
                restoreBestSize(bestSize);
            }
            return false;
        }
    }

    public boolean validRoomSize() {
        return this.roomWidth >= MIN_ROOM_SIDE
                && this.roomHeight >= MIN_ROOM_SIDE;
    }

    private boolean desiredSize(int targetWidth, int targetHeight) {
        return targetWidth == this.roomWidth
                && targetHeight == this.roomHeight;
    }

    private void restoreBestSize(SizeBackup backup) {
        xlowl = backup.getXlowl();
        ylowl = backup.getYlowl();
        xupr = backup.getXupr();
        yupr = backup.getYupr();
        roomWidth = backup.getRoomWidth();
        roomHeight = backup.getRoomHeight();
    }

    private boolean roomFits(Room dummyRoom) {
        // check if room is out of grid
        if (isOutOfBounds(dummyRoom)) {
            return false;
        }
        // check if dummyRoom can fit among other existing rooms
        for (Room room : GridCreator.rooms) {
            if (overlaps(dummyRoom, room)){
                return false;
            }
        }
        // check if dummyRoom can fit among other existing hallways
        for (Hallway hallway : GridCreator.hallways) {
            if (overlaps(dummyRoom, hallway)){
                return false;
            }
        }
        return true;
    }

    private void setSmallestSize(Door cdoor) {
        Direction outDir = cdoor.getDir();
        // set starting corner positions
        xlowl = cdoor.getXpos();
        xupr = cdoor.getXpos();
        ylowl = cdoor.getYpos();
        yupr = cdoor.getYpos();

        // adjust corner so we create a 2 X 3 or 3 x 2 starting room
        switch (outDir) {
            case NORTH:
                xlowl -= 1;
                xupr += 1;
                ylowl -= 1;
                break;
            case WEST:
                xupr += 1;
                ylowl -= 1;
                yupr +=1;
                break;
            case SOUTH:
                xlowl -= 1;
                xupr += 1;
                yupr += 1;
                break;
            case EAST:
                xlowl -= 1;
                ylowl -= 1;
                yupr += 1;
                break;
        }

        // calculate and set initial dimensions
        roomWidth = xupr - xlowl + 1;
        roomHeight = yupr - ylowl + 1;
    }

    private Door makeEnteringDoor(Hallway hallway) {
        Direction inDir = hallway.getEndDoor().getDir();
        Direction outDir = getOpositeDir(inDir);
        // make position of door in new room equal to incoming door of hallway
        xposd = hallway.getEndDoor().getXpos();
        yposd = hallway.getEndDoor().getYpos();
        // adjust location according to entering location
        switch (inDir) {
            case NORTH:
                yposd += 1;
                break;
            case WEST:
                xposd -= 1;
                break;
            case SOUTH:
                yposd -= 1;
                break;
            case EAST:
                xposd += 1;
                break;
        }
        return new Door(xposd, yposd, doorTile, outDir);
    }

    private void setDoors(RandomGen rgen, Door connectingDoor){
        int ndoors;
        HashSet<Direction> usedDirs = new HashSet<>(); // hold used directions
        // check if entering door is provided
        if (connectingDoor == null) {
            ndoors = rgen.random(1, 4); // first room: can be 1 or 4 doors
        } else {
            // if a hallways in entering this, then we must add connecting door
            ndoors = rgen.random(2, 4); // make sure has the entry and at least one exit
            usedDirs.add(connectingDoor.getDir()); // add opposite of entering direction
            doors.add(connectingDoor);
            ndoors -= 1;
        }

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

    private Direction getOpositeDir(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.SOUTH;
            case WEST:
                return Direction.EAST;
            case SOUTH:
                return Direction.NORTH;
            case EAST:
                return Direction.WEST;
        }
        return null;
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
        int randomIndex = rgen.random(0, doors.size() - 1);
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

    public int getRoomWidth() {
        return roomWidth;
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public boolean wasCreated(){
        return created;
    }

    public TETile getWallTile() {
        return wallTile;
    }
}
