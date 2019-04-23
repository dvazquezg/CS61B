package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class GridCreator {

    private int columns;     // The number of columns on the board (how wide it will be).
    private int rows;        // The number of rows on the board (how tall it will be).
    private int numRooms;    // The range of the number of rooms there can be.
    private ArrayList<Room> rooms;          // List of rooms in this grid
    private ArrayList<Hallway> hallways;    // List of hallways in this grid
    private TETile rWall = Tileset.WALL;    // Default room wall tile
    TETile[][] world;     // The world to be generated

    public GridCreator(int columns, int rows, RandomGen rgen){
        this.columns = columns;
        this.rows = rows;
        this.numRooms = rgen.random(6, 15);
        this.world = new TETile[columns][rows];
        System.out.println("rows: " + rows + ", cols: " + columns);
        System.out.println("Rooms to generate: " + this.numRooms);
        setup(rgen);
    }

    public void setup(RandomGen rgen) {
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
        System.out.println(testRoom1);
        System.out.println(testRoom2);
        System.out.println(testRoom3);
        System.out.println(testRoom4);
        System.out.println(testRoom5);
        System.out.println(testRoom6);
        rooms.add(testRoom1);
        rooms.add(testRoom2);
        rooms.add(testRoom3);
        rooms.add(testRoom4);
        rooms.add(testRoom5);
        rooms.add(testRoom6);
        System.out.println("Overlaps? " + testRoom1.overlaps(testRoom6));

        addRooms();
        // Create the first room and corridor.
        //rooms.add(new Room(columns, rows, rWall, rgen));

        /*
        rooms[0] = new Room(columns, rows, rgen);

        // create the first corridor
        hallways[0] = new Hallway(rooms[0], corridorLength, roomWidth, roomHeight, columns, rows, true);
        */

    }

    private void addRooms(){
        Random r = new Random();
        for (Room room : rooms) {
            for (int x = room.xlowl; x <= room.xupr; x += 1) {
                for (int y = room.ylowl; y <= room.yupr; y += 1) {
                    if (x > room.xlowl && x < room.xupr && y > room.ylowl) {
                        y = room.yupr; // jump to upper row to avoid center
                    }
                    world[x][y] = TETile.colorVariant(Tileset.WALL, 20, 20, 20, r);
                    //Tile current = new Tile(x, y);
                }
            }
        }
    }



    private void initialize() {
        // initialize tiles
        for (int x = 0; x < columns; x += 1) {
            for (int y = 0; y < rows; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] grid(){

        return world; // <<<<<<< change
    }



}
