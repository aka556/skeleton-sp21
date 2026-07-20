package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Computes the width of a hexagon with side length s at row y.
     * The hexagon has 2*s rows total.
     */
    private static int hexRowWidth(int s, int y) {
        if (y < s) {
            // Upper half: width increases by 2 each row
            return s + 2 * y;
        } else {
            // Lower half: width decreases by 2 each row
            return s + 2 * (2 * s - 1 - y);
        }
    }

    /**
     * Computes the x-offset from the left edge of the hexagon at row y.
     * This tells us how many blank spaces to skip before starting to draw.
     */
    private static int hexRowOffset(int s, int y) {
        if (y < s) {
            // Upper half: offset decreases by 1 each row
            return s - 1 - y;
        } else {
            // Lower half: offset increases by 1 each row
            return y - s + 1;
        }
    }

    /**
     * Adds a hexagon of side length s at position (px, py) to the world.
     * The hexagon is drawn using the given tile.
     * @param world the 2D tile array
     * @param px the x-coordinate of the bottom-left corner of the hexagon's bounding box
     * @param py the y-coordinate of the bottom-left corner of the hexagon's bounding box
     * @param s the side length of the hexagon
     * @param t the tile to use for drawing
     */
    public static void addHexagon(TETile[][] world, int px, int py, int s, TETile t) {
        int height = 2 * s;
        for (int y = 0; y < height; y++) {
            int worldY = py + y;
            int offset = hexRowOffset(s, y);
            int width = hexRowWidth(s, y);
            for (int x = 0; x < width; x++) {
                int worldX = px + offset + x;
                if (worldX >= 0 && worldX < world.length && worldY >= 0 && worldY < world[0].length) {
                    world[worldX][worldY] = t;
                }
            }
        }
    }

    /**
     * Picks a random tile from a set of terrain tiles.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * Computes the x-coordinate for a hexagon at column (col) with side length s.
     * Adjacent hexagons share an edge, so the x-spacing accounts for overlap.
     */
    private static int hexX(int col, int s) {
        // Each hexagon occupies 2*s - 1 columns (they share edges)
        return col * (2 * s - 1);
    }

    /**
     * Computes the y-coordinate for a hexagon at row (row) within column (col)
     * in a hexagonal tessellation.
     * For a standard tessellation of 19 hexagons, we have:
     *   Column 0: rows 0,1,2
     *   Column 1: rows 0,1,2,3
     *   Column 2: rows 0,1,2,3,4
     *   Column 3: rows 1,2,3,4
     *   Column 4: rows 2,3,4
     */
    private static int hexY(int col, int row, int s) {
        // Offset based on column for tessellation
        int yOffset;
        if (col < 3) {
            yOffset = col;
        } else {
            yOffset = col - 2;
        }
        return (row - yOffset) * s;
    }

    /**
     * Draws the full hexagonal tessellation with 19 hexagons.
     */
    public static void drawWorld(TETile[][] world, int s) {
        // Column 0: 3 hexagons
        for (int row = 0; row < 3; row++) {
            int x = hexX(0, s);
            int y = hexY(0, row, s);
            addHexagon(world, x, y, s, randomTile());
        }
        // Column 1: 4 hexagons
        for (int row = 0; row < 4; row++) {
            int x = hexX(1, s);
            int y = hexY(1, row, s);
            addHexagon(world, x, y, s, randomTile());
        }
        // Column 2: 5 hexagons
        for (int row = 0; row < 5; row++) {
            int x = hexX(2, s);
            int y = hexY(2, row, s);
            addHexagon(world, x, y, s, randomTile());
        }
        // Column 3: 4 hexagons
        for (int row = 1; row < 5; row++) {
            int x = hexX(3, s);
            int y = hexY(3, row, s);
            addHexagon(world, x, y, s, randomTile());
        }
        // Column 4: 3 hexagons
        for (int row = 2; row < 5; row++) {
            int x = hexX(4, s);
            int y = hexY(4, row, s);
            addHexagon(world, x, y, s, randomTile());
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // Initialize world with NOTHING tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // Draw hexagonal tessellation with side length 3
        drawWorld(world, 3);

        ter.renderFrame(world);
    }
}
