package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * System constants
 */
public class Constants {
    public enum Direction { NORTH, WEST, SOUTH, EAST }
    public enum Action { NEW, LOAD }
    public static final String KEYSET1 = "WASDwasd";
    public static final int MIN_ROOM_SIDE = 4; // min can be 4
    public static final int MAX_ROOM_SIDE =  8;
    public static final TETile DOORTILE = Tileset.SAND;
    public static final TETile WALLTILE = Tileset.WALL;
    public static final TETile FLOORTILE = Tileset.FLOOR;
    public static final int MIN_HALLWAY_LEN = 2; // min can be 2
    public static final int MAX_HALLWAY_LEN = 6;
    public static final int MIN_DOORS_PER_ROOM = 4; // min 1
    public static final int MAX_DOOR_PER_ROOM = 4; // max 4
}
