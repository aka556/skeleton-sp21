package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;

/**
 * Represents the state of the game for saving and loading.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public long seed;
    public int avatarX;
    public int avatarY;
    public TETile[][] world;
    public String moveHistory;

    public GameState(long seed, int avatarX, int avatarY, TETile[][] world, String moveHistory) {
        this.seed = seed;
        this.avatarX = avatarX;
        this.avatarY = avatarY;
        this.world = world;
        this.moveHistory = moveHistory;
    }
}
