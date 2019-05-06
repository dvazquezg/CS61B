package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * System constants
 */
public class Constants {
    /////////// GRID GENERATION ///////////
    public enum Direction { NORTH, WEST, SOUTH, EAST }
    public enum Action { NEW, LOAD }
    public static final String KEYSET1 = "WASDwasd"; // not used in game (experimental)
    public static final int MIN_ROOM_SIDE = 4; // min can be 4
    public static final int MAX_ROOM_SIDE =  8;
    public static final TETile DOORTILE = Tileset.SAND;
    public static final TETile WALLTILE = Tileset.WALL;
    public static final TETile FLOORTILE = Tileset.FLOOR;
    public static final TETile NOTHING = Tileset.NOTHING;
    public static final int MIN_HALLWAY_LEN = 2; // min can be 2
    public static final int MAX_HALLWAY_LEN = 6;
    public static final int MIN_DOORS_PER_ROOM = 4; // min 1
    public static final int MAX_DOOR_PER_ROOM = 4; // max 4
    /////////// GAME DYNAMICS ///////////
    public static final int INIT_LIVES = 3;
    public static final TETile AVATARTILE = Tileset.AVATAR;
    public static final TETile GHOSTTILE = Tileset.GHOST;
    public static final TETile LOCKEDDOORTILE = Tileset.LOCKED_DOOR;
    public static final TETile KEYTILE = Tileset.KEY;
    public static final int EXPLORERADIUS = 3; // maximum 4
}
