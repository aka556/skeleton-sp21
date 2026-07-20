package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates a random world with rooms and hallways.
 */
public class WorldGenerator {
    private int width;
    private int height;
    private Random random;
    private TETile[][] world;
    private List<Room> rooms;

    public WorldGenerator(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.random = new Random(seed);
        this.world = new TETile[width][height];
        this.rooms = new ArrayList<>();
    }

    /**
     * Generates the world and returns the tile array.
     */
    public TETile[][] generate() {
        // Initialize world with NOTHING
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // Generate rooms
        generateRooms();

        // Connect rooms with hallways
        connectRooms();

        // Add walls around floors
        addWalls();

        return world;
    }

    /**
     * Generates random rooms.
     */
    private void generateRooms() {
        int numRooms = 6 + random.nextInt(7); // 6-12 rooms

        for (int i = 0; i < numRooms * 3; i++) { // Try more times than needed
            if (rooms.size() >= numRooms) {
                break;
            }

            int w = 3 + random.nextInt(6); // width 3-8
            int h = 3 + random.nextInt(5); // height 3-7
            int x = 1 + random.nextInt(width - w - 2); // leave margin
            int y = 1 + random.nextInt(height - h - 2);

            Room newRoom = new Room(x, y, w, h);

            // Check for overlap with existing rooms
            boolean overlaps = false;
            for (Room room : rooms) {
                if (newRoom.overlaps(room)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                rooms.add(newRoom);
                drawRoom(newRoom);
            }
        }
    }

    /**
     * Draws a room on the world.
     */
    private void drawRoom(Room room) {
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Connects all rooms with hallways using a simple approach.
     */
    private void connectRooms() {
        if (rooms.size() < 2) {
            return;
        }

        // Connect each room to the next one
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room current = rooms.get(i);
            Room next = rooms.get(i + 1);
            drawHallway(current.centerX(), current.centerY(),
                       next.centerX(), next.centerY());
        }

        // Add some extra connections for more interesting layout
        if (rooms.size() > 3) {
            int extraConnections = random.nextInt(rooms.size() / 2);
            for (int i = 0; i < extraConnections; i++) {
                int idx1 = random.nextInt(rooms.size());
                int idx2 = random.nextInt(rooms.size());
                if (idx1 != idx2) {
                    Room r1 = rooms.get(idx1);
                    Room r2 = rooms.get(idx2);
                    drawHallway(r1.centerX(), r1.centerY(),
                               r2.centerX(), r2.centerY());
                }
            }
        }
    }

    /**
     * Draws a hallway between two points with possible turns.
     */
    private void drawHallway(int x1, int y1, int x2, int y2) {
        int currentX = x1;
        int currentY = y1;

        // Decide whether to go horizontal first or vertical first
        boolean horizontalFirst = random.nextBoolean();

        if (horizontalFirst) {
            // Go horizontal first
            while (currentX != x2) {
                if (currentX >= 0 && currentX < width && currentY >= 0 && currentY < height) {
                    if (world[currentX][currentY] == Tileset.NOTHING) {
                        world[currentX][currentY] = Tileset.FLOOR;
                    }
                }
                currentX += (x2 > x1) ? 1 : -1;
            }
            // Then go vertical
            while (currentY != y2) {
                if (currentX >= 0 && currentX < width && currentY >= 0 && currentY < height) {
                    if (world[currentX][currentY] == Tileset.NOTHING) {
                        world[currentX][currentY] = Tileset.FLOOR;
                    }
                }
                currentY += (y2 > y1) ? 1 : -1;
            }
        } else {
            // Go vertical first
            while (currentY != y2) {
                if (currentX >= 0 && currentX < width && currentY >= 0 && currentY < height) {
                    if (world[currentX][currentY] == Tileset.NOTHING) {
                        world[currentX][currentY] = Tileset.FLOOR;
                    }
                }
                currentY += (y2 > y1) ? 1 : -1;
            }
            // Then go horizontal
            while (currentX != x2) {
                if (currentX >= 0 && currentX < width && currentY >= 0 && currentY < height) {
                    if (world[currentX][currentY] == Tileset.NOTHING) {
                        world[currentX][currentY] = Tileset.FLOOR;
                    }
                }
                currentX += (x2 > x1) ? 1 : -1;
            }
        }

        // Draw the final point
        if (currentX >= 0 && currentX < width && currentY >= 0 && currentY < height) {
            if (world[currentX][currentY] == Tileset.NOTHING) {
                world[currentX][currentY] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Adds walls around floor tiles.
     */
    private void addWalls() {
        TETile[][] copy = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                copy[x][y] = world[x][y];
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (copy[x][y] == Tileset.NOTHING) {
                    // Check if adjacent to a floor tile
                    if (isAdjacentToFloor(copy, x, y)) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    /**
     * Checks if a position is adjacent to a floor tile.
     */
    private boolean isAdjacentToFloor(TETile[][] grid, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (grid[nx][ny] == Tileset.FLOOR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the list of generated rooms.
     */
    public List<Room> getRooms() {
        return rooms;
    }
}
