package byow.Core;

import byow.TileEngine.TETile;

import static byow.Core.Constants.AVATARTILE;
import static byow.Core.Constants.INIT_LIVES;

public class Avatar implements Creature {

    SimplePoint location;
    private int lives = INIT_LIVES;
    private String name;
    private String description;
    private TETile tile = AVATARTILE;

    public Avatar(SimplePoint location, String name) {
        this.location = new SimplePoint(location.getXpos(), location.getYpos());
        this.name = name;
        this.description = "Main player";
    }

    @Override
    public int getX() {
        return location.getXpos();
    }

    @Override
    public int getY() {
        return location.getYpos();
    }

    @Override
    public boolean isAlive() {
        return lives > 0;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void increaseLiveByOne() {
        lives += 1;
    }

    @Override
    public void decreaseLiveByOne() {
        lives -= 1;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;

    }

    @Override
    public TETile getTile() {
        return tile;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPos(SimplePoint location) {
        this.location.setX(location.getXpos());
        this.location.setY(location.getYpos());
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setTile(TETile tile) {
        this.tile = tile;
    }

    @Override
    public void kill() {
        lives = 0;
    }

    @Override
    public void moveNorth() {
        this.location.setY(location.getYpos() + 1);
    }

    @Override
    public void moveWest() {
        this.location.setX(location.getXpos() - 1);
    }

    @Override
    public void moveSouth() {
        this.location.setY(location.getYpos() - 1);
    }

    @Override
    public void moveEast() {
        this.location.setX(location.getXpos() + 1);
    }

    @Override
    public void setLocation(SimplePoint location) {
        this.location = location;
    }

    @Override
    public SimplePoint getLocation() {
        return location;
    }

}
