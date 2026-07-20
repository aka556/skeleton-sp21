package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;

/**
 * Represents the state of the game for saving and loading.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private long seed;
    private int avatarX;
    private int avatarY;
    private TETile[][] world;
    private String moveHistory;

    public GameState(long seed, int avatarX, int avatarY,
                     TETile[][] world, String moveHistory) {
        this.seed = seed;
        this.avatarX = avatarX;
        this.avatarY = avatarY;
        this.world = world;
        this.moveHistory = moveHistory;
    }

    public long getSeed() {
        return seed;
    }

    public int getAvatarX() {
        return avatarX;
    }

    public int getAvatarY() {
        return avatarY;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public String getMoveHistory() {
        return moveHistory;
    }
}
