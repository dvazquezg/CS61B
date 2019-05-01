package byow.Core;

import byow.TileEngine.TETile;

public interface Creature {
    int getX();
    int getY();
    boolean isAlive();
    int getLives();
    void increaseLiveByOne();
    void decreaseLiveByOne();
    void setLives(int lives);
    TETile getTile();
    String getName();
    String getDescription();
    SimplePoint getLocation();
    void setPos(SimplePoint location);
    void setName(String name);
    void setDescription(String description);
    void setTile(TETile tile);
    void kill();
    void moveNorth();
    void moveWest();
    void moveSouth();
    void moveEast();
    void setLocation(SimplePoint location);
}
