package byow.Core;

public class SizeBackup {
    protected int xlowl;        // The x coordinate of the lower left tile of the room.
    protected int ylowl;        // The y coordinate of the lower left tile of the room.
    protected int xupr;         // The x coordinate of the upper right tile of the room.
    protected int yupr;         // The y coordinate of the upper right tile of the room.
    protected int roomWidth;    // The room's width in tiles
    protected int roomHeight;   // The room's height in tiles

    public SizeBackup(int xlowl, int ylowl, int xupr, int yupr, int roomWidth, int roomHeight) {
        this.xlowl = xlowl;
        this.ylowl = ylowl;
        this.xupr = xupr;
        this.yupr = yupr;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
    }

    public int getXlowl() {
        return xlowl;
    }

    public int getYlowl() {
        return ylowl;
    }

    public int getXupr() {
        return xupr;
    }

    public int getYupr() {
        return yupr;
    }

    public int getRoomWidth() {
        return roomWidth;
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public void setXlowl(int xlowl) {
        this.xlowl = xlowl;
    }

    public void setYlowl(int ylowl) {
        this.ylowl = ylowl;
    }

    public void setXupr(int xupr) {
        this.xupr = xupr;
    }

    public void setYupr(int yupr) {
        this.yupr = yupr;
    }

    public void setRoomWidth(int roomWidth) {
        this.roomWidth = roomWidth;
    }

    public void setRoomHeight(int roomHeight) {
        this.roomHeight = roomHeight;
    }
}
