package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Represents the player's avatar in the world.
 */
public class Avatar {
    private int x;
    private int y;
    private TETile[][] world;

    public Avatar(int x, int y, TETile[][] world) {
        this.x = x;
        this.y = y;
        this.world = world;
        // Place avatar on the world
        world[x][y] = Tileset.AVATAR;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Moves the avatar in the specified direction.
     * @param direction 'w' for up, 's' for down, 'a' for left, 'd' for right
     * @return true if the move was successful
     */
    public boolean move(char direction) {
        int newX = x;
        int newY = y;

        switch (Character.toLowerCase(direction)) {
            case 'w':
                newY += 1;
                break;
            case 's':
                newY -= 1;
                break;
            case 'a':
                newX -= 1;
                break;
            case 'd':
                newX += 1;
                break;
            default:
                return false;
        }

        // Check bounds
        if (newX < 0 || newX >= world.length || newY < 0 || newY >= world[0].length) {
            return false;
        }

        // Check if the new position is walkable
        if (world[newX][newY] == Tileset.FLOOR || world[newX][newY] == Tileset.NOTHING) {
            // Remove avatar from old position
            world[x][y] = Tileset.FLOOR;
            // Move to new position
            x = newX;
            y = newY;
            world[x][y] = Tileset.AVATAR;
            return true;
        }

        return false;
    }
}
