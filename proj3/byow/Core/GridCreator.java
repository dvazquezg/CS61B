package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import static byow.Core.Constants.*;

public class GridCreator {

    protected static int columns;     // The number of columns on the board (how wide it will be).
    protected static int rows;        // The number of rows on the board (how tall it will be).
    protected int numRooms;    // The range of the number of rooms there can be.
    protected static ArrayList<Room> rooms;          // List of rooms in this grid
    protected ArrayList<Room> availableRooms; // Dynamic list of room who have disconnected doors
    protected static ArrayList<Hallway> hallways;    // List of hallways in this grid
    protected TETile rWall = Tileset.WALL;    // Default room's wall tile
    protected TETile[][] world;     // The world to be generated

    public GridCreator(int columns, int rows, RandomGen rgen){
        this.columns = columns;
        this.rows = rows;
        this.numRooms = rgen.random(6, 15);
        this.world = new TETile[columns][rows];
        System.out.println("rows: " + rows + ", cols: " + columns);
        System.out.println("Rooms to generate: " + this.numRooms);
        //setuptest(rgen);
        setup(rgen);
    }

    public void setup(RandomGen rgen) {
        initialize(); // fill grid with empty tiles

        // Create the rooms array with a random size.
        rooms = new ArrayList<>();
        availableRooms = new ArrayList<>();

        // There should be one less corridor than there is rooms.
        hallways = new ArrayList<>();

        // create first room
        Room firstRoom = new Room(columns, rows, rWall, rgen);
        rooms.add(firstRoom);
        availableRooms.add(firstRoom);

        // main loop, fills
        while (rooms.size() < numRooms) {
            Room currentRoom = getRandRoomWithAvalDoors(rgen);

            Hallway newHallway = new Hallway(columns, rows, rWall, currentRoom, rgen);
            hallways.add(newHallway);

            //Room newRoom = new Room(columns, rows, rWall, newHallway, rgen);
            //rooms.add(newRoom);
            //availableRooms.add(newRoom);

            //removeRoomsWithNoAvalDoors();

            break;
        }



        // from current rooms chose a room with available doors
        // create hallway by passing selected room
        // mark door of room used to connect hallway as connected
        // create a room at the end of hallway and add it to list
        // repeat until number of room is completed
        // when adding doors we will carve only connecting doors.



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
        int ramdomIndex = rgen.random(0, availableRooms.size() -1);
        return availableRooms.get(ramdomIndex);
    }

    private void removeRoomsWithNoAvalDoors(){
        availableRooms.removeIf(room -> (!room.hasAvailableDoors()));
    }

    public void setuptest(RandomGen rgen) {
        initialize();

        // Create the rooms array with a random size.
        rooms = new ArrayList<>();

        // There should be one less corridor than there is rooms.
        hallways = new ArrayList<>();

        Room testRoom1 = new Room(10, 10, 5, 5);
        Room testRoom2 = new Room(6, 14, 5, 5);
        Room testRoom3 = new Room(14, 6, 5, 5);
        Room testRoom4 = new Room(6, 6, 5, 5);
        Room testRoom5 = new Room(14, 14, 5, 5);
        Room testRoom6 = new Room(20, 20, 5, 5);
        Room testOutofBounds = new Room(0, 0, 80, 30);
        System.out.println(testRoom1);
        System.out.println(testRoom2);
        System.out.println(testRoom3);
        System.out.println(testRoom4);
        System.out.println(testRoom5);
        System.out.println(testRoom6);
        //rooms.add(testOutofBounds);
        rooms.add(testRoom1);
        rooms.add(testRoom2);
        rooms.add(testRoom3);
        rooms.add(testRoom4);
        rooms.add(testRoom5);
        rooms.add(testRoom6);
        System.out.println("Overlaps? " + overlaps(testRoom1, testRoom5));
        System.out.println("Out of bounds? " + isOutOfBounds(testOutofBounds));

        addRoomsToGrid();

    }

    private void addRoomsToGrid(){
        Random r = new Random();
        for (Room room : rooms) {
            for (int x = room.xlowl; x <= room.xupr; x += 1) {
                for (int y = room.ylowl; y <= room.yupr; y += 1) {
                    if (x > room.xlowl && x < room.xupr && y > room.ylowl && y < room.yupr) {
                        //y = room.yupr; // jump to upper row to avoid center
                        world[x][y] = Tileset.FLOOR;
                    } else {
                        world[x][y] = TETile.colorVariant(Tileset.WALL, 20, 20, 20, r);
                        //Tile current = new Tile(x, y);
                    }
                }
            }
        }
    }

    private void addHallwaysToGrid() {
        Random r = new Random();
        for (Hallway hallway : hallways) {
            if (hallway.dir == Direction.EAST || hallway.dir == Direction.WEST) {
                addHorizontalHallway(hallway, r);
            } else {
                addVerticalHallway(hallway, r);
            }
        }
    }

    private void addHorizontalHallway(Hallway hallway, Random r) {
        for (int x = hallway.xlowl; x <= hallway.xupr; x += 1) {
            for (int y = hallway.ylowl; y <= hallway.yupr; y += 1) {
                if (x > hallway.xlowl && x < hallway.xupr && y > hallway.ylowl && y < hallway.yupr) {
                    //y = hallway.yupr; // jump to upper row to avoid center
                    world[x][y] = Tileset.FLOOR;
                } else {
                    world[x][y] = TETile.colorVariant(Tileset.WALL, 20, 20, 20, r);
                    //Tile current = new Tile(x, y);
                }
            }
        }
    }

    private void addVerticalHallway(Hallway hallway, Random r) {

    }

    private void addDoorsToGrid() {
        for (Room room : rooms) {
            for (Door door : room.doors) {
                if (door.isConnected()) {
                    door.setTile(Tileset.FLOOR); // if connected the carve door
                }
                world[door.getXpos()][door.getYpos()] = door.getTile();
            }
        }

        for (Hallway hallway : hallways) {
            for (Door door : hallway.doors) {
                if (door.isConnected()) {
                    door.setTile(Tileset.FLOOR); // if connected the carve door
                }
                world[door.getXpos()][door.getYpos()] = door.getTile();
            }
        }
    }

    public static boolean overlaps(InteriorSpace space1, InteriorSpace space2){
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

    public static boolean isOutOfBounds(InteriorSpace target){
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
    public TETile[][] grid(){
        return world;
    }
}
