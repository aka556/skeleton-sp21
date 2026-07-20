package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int HUD_HEIGHT = 4;

    private TETile[][] world;
    private Avatar avatar;
    private long seed;
    private boolean gameRunning;
    private StringBuilder moveHistory;
    private GameState savedState;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // Initialize StdDraw
        ter.initialize(WIDTH, HEIGHT + HUD_HEIGHT);

        // Show main menu
        showMainMenu();
    }

    /**
     * Shows the main menu and handles user input.
     */
    private void showMainMenu() {
        gameRunning = true;

        while (gameRunning) {
            // Draw menu
            StdDraw.clear(Color.BLACK);
            Font titleFont = new Font("Monaco", Font.BOLD, 40);
            Font menuFont = new Font("Monaco", Font.PLAIN, 20);
            StdDraw.setFont(titleFont);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 5, "CS61BYoW");
            StdDraw.setFont(menuFont);
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "New World (N)");
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 2, "Load World (L)");
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 4, "Quit (Q)");
            StdDraw.show();

            // Handle input
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (key) {
                    case 'n':
                        handleNewWorld();
                        break;
                    case 'l':
                        handleLoadWorld();
                        break;
                    case 'q':
                        gameRunning = false;
                        break;
                }
            }

            StdDraw.pause(50);
        }
    }

    /**
     * Handles the new world creation process.
     */
    private void handleNewWorld() {
        // Get seed from user
        StringBuilder seedInput = new StringBuilder();
        boolean enteringSeed = true;

        while (enteringSeed) {
            StdDraw.clear(Color.BLACK);
            Font font = new Font("Monaco", Font.PLAIN, 20);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 2, "Enter Seed:");
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, seedInput.toString() + "_");
            StdDraw.show();

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'S' || key == 's') {
                    enteringSeed = false;
                } else if (Character.isDigit(key)) {
                    seedInput.append(key);
                }
            }

            StdDraw.pause(50);
        }

        // Parse seed and create world
        if (seedInput.length() > 0) {
            seed = Long.parseLong(seedInput.toString());
            createWorld(seed);
            startGameLoop();
        }
    }

    /**
     * Handles loading a saved world.
     */
    private void handleLoadWorld() {
        GameState loaded = loadGame();
        if (loaded != null) {
            seed = loaded.seed;
            world = loaded.world;
            moveHistory = new StringBuilder(loaded.moveHistory);
            avatar = new Avatar(loaded.avatarX, loaded.avatarY, world);
            startGameLoop();
        }
    }

    /**
     * Creates a new world with the given seed.
     */
    private void createWorld(long seed) {
        WorldGenerator generator = new WorldGenerator(WIDTH, HEIGHT, seed);
        world = generator.generate();

        // Place avatar at the center of the first room
        if (generator.getRooms().size() > 0) {
            Room firstRoom = generator.getRooms().get(0);
            avatar = new Avatar(firstRoom.centerX(), firstRoom.centerY(), world);
        }

        moveHistory = new StringBuilder();
    }

    /**
     * Starts the main game loop.
     */
    private void startGameLoop() {
        ter.initialize(WIDTH, HEIGHT + HUD_HEIGHT);

        while (gameRunning) {
            // Render world
            renderWorld();

            // Handle input
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                // Check for quit command
                if (key == ':') {
                    if (StdDraw.hasNextKeyTyped()) {
                        char nextKey = Character.toLowerCase(StdDraw.nextKeyTyped());
                        if (nextKey == 'q') {
                            saveGame();
                            gameRunning = false;
                            return;
                        }
                    }
                }

                // Handle movement
                if (Character.toLowerCase(key) == 'w' || Character.toLowerCase(key) == 'a' ||
                    Character.toLowerCase(key) == 's' || Character.toLowerCase(key) == 'd') {
                    if (avatar.move(key)) {
                        moveHistory.append(key);
                    }
                }
            }

            StdDraw.pause(50);
        }
    }

    /**
     * Renders the world and HUD.
     */
    private void renderWorld() {
        // Draw world
        ter.renderFrame(world);

        // Draw HUD
        drawHUD();
    }

    /**
     * Draws the Heads Up Display.
     */
    private void drawHUD() {
        // Draw HUD background
        StdDraw.setPenColor(Color.DARK_GRAY);
        StdDraw.filledRectangle(WIDTH / 2.0, HEIGHT + HUD_HEIGHT / 2.0,
                               WIDTH / 2.0, HUD_HEIGHT / 2.0);

        // Draw tile description under mouse
        Font hudFont = new Font("Monaco", Font.PLAIN, 14);
        StdDraw.setFont(hudFont);
        StdDraw.setPenColor(Color.WHITE);

        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT) {
            TETile tile = world[mouseX][mouseY];
            String description = getTileDescription(tile);
            StdDraw.text(10, HEIGHT + HUD_HEIGHT / 2.0, description);
        }

        // Draw seed info
        StdDraw.text(WIDTH - 15, HEIGHT + HUD_HEIGHT / 2.0, "Seed: " + seed);

        StdDraw.show();
    }

    /**
     * Returns a description for the given tile.
     */
    private String getTileDescription(TETile tile) {
        if (tile == Tileset.FLOOR) return "floor";
        if (tile == Tileset.WALL) return "wall";
        if (tile == Tileset.AVATAR) return "you";
        if (tile == Tileset.NOTHING) return "nothing";
        if (tile == Tileset.LOCKED_DOOR) return "locked door";
        if (tile == Tileset.UNLOCKED_DOOR) return "unlocked door";
        return "unknown";
    }

    /**
     * Saves the current game state.
     */
    private void saveGame() {
        GameState state = new GameState(seed, avatar.getX(), avatar.getY(),
                                       world, moveHistory.toString());
        try {
            FileOutputStream fileOut = new FileOutputStream("savefile.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(state);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a saved game state.
     */
    private GameState loadGame() {
        try {
            FileInputStream fileIn = new FileInputStream("savefile.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameState state = (GameState) in.readObject();
            in.close();
            fileIn.close();
            return state;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Parse input
        String lowerInput = input.toLowerCase();

        // Check if it starts with 'l' (load)
        if (lowerInput.startsWith("l")) {
            GameState loaded = loadGame();
            if (loaded == null) {
                return new TETile[WIDTH][HEIGHT]; // Return empty world if no save
            }

            seed = loaded.seed;
            world = loaded.world;
            moveHistory = new StringBuilder(loaded.moveHistory);
            avatar = new Avatar(loaded.avatarX, loaded.avatarY, world);

            // Process remaining input
            processInput(lowerInput.substring(1));
        }
        // Check if it starts with 'n' (new world)
        else if (lowerInput.startsWith("n")) {
            // Find the seed (digits between 'n' and 's')
            int sIndex = lowerInput.indexOf('s', 1);
            if (sIndex == -1) {
                sIndex = lowerInput.length();
            }

            String seedStr = lowerInput.substring(1, sIndex);
            if (seedStr.length() > 0) {
                seed = Long.parseLong(seedStr);
                createWorld(seed);

                // Process remaining input after 's'
                if (sIndex < lowerInput.length()) {
                    processInput(lowerInput.substring(sIndex + 1));
                }
            }
        }

        return world;
    }

    /**
     * Processes input characters for movement and commands.
     */
    private void processInput(String input) {
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            // Check for quit command
            if (c == ':' && i + 1 < input.length() && input.charAt(i + 1) == 'q') {
                saveGame();
                i += 2;
                continue;
            }

            // Handle movement
            if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
                avatar.move(c);
                moveHistory.append(c);
            }

            i++;
        }
    }
}
