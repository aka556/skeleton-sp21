package byow.Core;

import java.io.Serializable;

/**
 * Represents the state of the game for saving and loading.
 * Only saves seed and move history - world is regenerated on load.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private long seed;
    private String moveHistory;

    public GameState(long seed, String moveHistory) {
        this.seed = seed;
        this.moveHistory = moveHistory;
    }

    public long getSeed() {
        return seed;
    }

    public String getMoveHistory() {
        return moveHistory;
    }
}
