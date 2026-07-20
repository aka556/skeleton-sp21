package byow.Core;

/**
 * Represents a room in the world.
 */
public class Room {
    private int x;      // bottom-left x coordinate
    private int y;      // bottom-left y coordinate
    private int width;
    private int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /** Returns the center x coordinate of this room. */
    public int centerX() {
        return x + width / 2;
    }

    /** Returns the center y coordinate of this room. */
    public int centerY() {
        return y + height / 2;
    }

    /** Returns true if this room overlaps with the other room. */
    public boolean overlaps(Room other) {
        return this.x < other.x + other.width + 1
            && this.x + this.width + 1 > other.x
            && this.y < other.y + other.height + 1
            && this.y + this.height + 1 > other.y;
    }
}
