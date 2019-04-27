package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

public class GridCreator {

    protected static int columns;     // The number of columns on the board (how wide it will be).
    protected static int rows;        // The number of rows on the board (how tall it will be).
    protected int numRooms;           // The range of the number of rooms there can be.
    protected static ArrayList<Room> rooms;   // List of rooms in this grid
    protected ArrayList<Room> availableRooms; // Dynamic list of room who have disconnected doors
    protected static ArrayList<Hallway> hallways;    // List of hallways in this grid
    protected TETile[][] world;   // The world to be generated

    public GridCreator(int columns, int rows, RandomGen rgen) {
        this.columns = columns;
        this.rows = rows;
        this.numRooms = rgen.random(10, 15);
        this.world = new TETile[columns][rows];
        setup(rgen);
    }

    public void setup(RandomGen rgen) {
        initialize(); // fill grid with empty tiles

        // Create the rooms array with a random size.
        rooms = new ArrayList<>();
        availableRooms = new ArrayList<>();

        // There should be one less corridor than there is rooms.
        hallways = new ArrayList<>();

        /////////////// COLLISION TEST
        //Room trickyRoom = new Room(20, 15, 8, 5);
        //rooms.add(trickyRoom);
        //Room trickyRoom2 = new Room(20, 15, 8, 5);
        //rooms.add(trickyRoom2);
        ///////////// END TEST

        // create first room
        Room firstRoom = new Room(columns, rows, rgen);
        rooms.add(firstRoom);
        availableRooms.add(firstRoom);

        // main loop, creates world
        while (hasDisconnectedDoors()) {
            Room currentRoom = getRandRoomWithAvalDoors(rgen);
            Hallway newHallway = new Hallway(currentRoom, rgen);
            // if hallway was created, try to create a room at the end
            if (newHallway.wasCreated()) {
                hallways.add(newHallway);
                Room newRoom = new Room(newHallway, rgen);
                if (newRoom.wasCreated()) {
                    rooms.add(newRoom);
                    availableRooms.add(newRoom);
                }
            }
            removeRoomsWithNoAvalDoors();
        }

        // add rooms to grid
        addRoomsToGrid();
        // add hallways to grid
        addHallwaysToGrid();
        // add door after adding all rooms and hallways
        addDoorsToGrid();
    }

    private Room getRandRoomWithAvalDoors(RandomGen rgen) {
        if (availableRooms.size() == 0) {
            return null;
        }
        int ramdomIndex = rgen.random(0, availableRooms.size() - 1);
        return availableRooms.get(ramdomIndex);
    }

    private void removeRoomsWithNoAvalDoors() {
        availableRooms.removeIf(room -> (!room.hasAvailableDoors()));
    }

    private void addRoomsToGrid() {
        Random r = new Random();
        for (Room room : rooms) {
            for (int x = room.xlowl; x <= room.xupr; x += 1) {
                for (int y = room.ylowl; y <= room.yupr; y += 1) {
                    if (x > room.xlowl && x < room.xupr && y > room.ylowl && y < room.yupr) {
                        world[x][y] = Constants.FLOORTILE;
                    } else {
                        world[x][y] = TETile.colorVariant(room.getWallTile(), 20, 20, 20, r);
                    }
                }
            }
            //System.out.println("printing: " + room);
        }
    }

    private void addHallwaysToGrid() {
        Random r = new Random();
        for (Hallway hallway : hallways) {
            for (int x = hallway.xlowl; x <= hallway.xupr; x += 1) {
                for (int y = hallway.ylowl; y <= hallway.yupr; y += 1) {
                    if (x > hallway.xlowl && x < hallway.xupr
                            && y > hallway.ylowl && y < hallway.yupr) {
                        //y = hallway.yupr; // jump to upper row to avoid center
                        world[x][y] = Constants.FLOORTILE;
                    } else {
                        world[x][y] = TETile.colorVariant(hallway.getWallTile(), 20, 20, 20, r);
                    }
                }
            }
        }
    }

    private void addDoorsToGrid() {
        for (Room room : rooms) {
            if (room.doors == null) {
                continue;
            }
            for (Door door : room.doors) {
                if (door.isConnected()) {
                    door.setTile(Constants.FLOORTILE); // if connected then is carved show floor
                }
                world[door.getXpos()][door.getYpos()] = door.getTile();
            }
        }

        for (Hallway hallway : hallways) {
            if (hallway.doors == null) {
                continue;
            }
            for (Door door : hallway.doors) {
                if (door.isConnected()) {
                    door.setTile(Constants.FLOORTILE); // if connected the carve door
                }
                world[door.getXpos()][door.getYpos()] = door.getTile();
            }
        }
    }

    public boolean hasDisconnectedDoors() {
        for (Room room : rooms) {
            if (room.doors == null) {
                continue;
            }
            for (Door door : room.doors) {
                if (!door.isConnected()) {
                    return true;
                }
            }
        }

        for (Hallway hallway : hallways) {
            if (hallway.doors == null) {
                continue;
            }
            for (Door door : hallway.doors) {
                if (!door.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean overlaps(InteriorSpace space1, InteriorSpace space2) {
        // Exclude parts where they do not overlap
        // If one rectangle is on left side of other
        if (space2.getxlowl() > space1.getxupr() || space1.getxlowl() > space2.getxupr()) {
            return false;
        }
        // If one rectangle is above other
        if (space2.getylowl() > space1.getyupr() || space1.getylowl() > space2.getyupr()) {
            return false;
        }
        return true;
    }

    public static boolean isOutOfBounds(InteriorSpace target) {
        // check if given space is within boundaries of grid
        if (target.getxlowl() >= 0 && target.getxupr() < columns
            && target.getylowl() >= 0 && target.getyupr() < rows) {
            return false;
        }
        return true;
    }


    /**
     * Fill all grid with Tileset.NOTHING or similar
     */
    private void initialize() {
        // initialize tiles
        for (int x = 0; x < columns; x += 1) {
            for (int y = 0; y < rows; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Return the grid
     * @return world grid
     */
    public TETile[][] grid() {
        return world;
    }
}
