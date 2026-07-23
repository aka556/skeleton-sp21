package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Networking.BYOWServer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;

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

    /**
     * Method used for exploring a fresh world.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT + HUD_HEIGHT);
        showMainMenu();
    }

    /**
     * Shows the main menu and handles user input.
     */
    private void showMainMenu() {
        gameRunning = true;

        while (gameRunning) {
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
                    default:
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

        if (seedInput.length() > 0) {
            this.seed = Long.parseLong(seedInput.toString());
            createWorld(this.seed);
            startGameLoop(null);
        }
    }

    /**
     * Handles loading a saved world.
     */
    private void handleLoadWorld() {
        loadAndRestoreGame(null);
        if (this.world != null) {
            startGameLoop(null);
        }
    }

    /**
     * Creates a new world with the given seed.
     */
    private void createWorld(long worldSeed) {
        WorldGenerator generator = new WorldGenerator(WIDTH, HEIGHT, worldSeed);
        world = generator.generate();

        if (generator.getRooms().size() > 0) {
            Room firstRoom = generator.getRooms().get(0);
            avatar = new Avatar(firstRoom.centerX(), firstRoom.centerY(), world);
        }

        moveHistory = new StringBuilder();
    }

    /**
     * Starts the main game loop.
     * @param server The BYOWServer for remote play, or null for local play
     */
    private void startGameLoop(BYOWServer server) {
        ter.initialize(WIDTH, HEIGHT + HUD_HEIGHT);

        if (server != null) {
            server.sendCanvasConfig(WIDTH * 16, (HEIGHT + HUD_HEIGHT) * 16);
            server.sendCanvas();
        }

        while (gameRunning) {
            renderWorld();

            if (server != null) {
                server.sendCanvas();
            }

            if (!processInput(server)) {
                continue;
            }

            StdDraw.pause(50);
        }
    }

    /**
     * Processes input from keyboard or remote client.
     * @return true if input was processed
     */
    private boolean processInput(BYOWServer server) {
        boolean hasInput;
        char key;

        if (server != null) {
            hasInput = server.clientHasKeyTyped();
            if (!hasInput) {
                return false;
            }
            key = server.clientNextKeyTyped();
        } else {
            hasInput = StdDraw.hasNextKeyTyped();
            if (!hasInput) {
                return false;
            }
            key = StdDraw.nextKeyTyped();
        }

        if (key == ':') {
            return handleQuitCommand(server);
        }

        handleMovement(key);
        return true;
    }

    /**
     * Handles quit command.
     * @return true if command was processed
     */
    private boolean handleQuitCommand(BYOWServer server) {
        boolean hasNext;
        char nextKey;

        if (server != null) {
            hasNext = server.clientHasKeyTyped();
            if (!hasNext) {
                return false;
            }
            nextKey = server.clientNextKeyTyped();
        } else {
            hasNext = StdDraw.hasNextKeyTyped();
            if (!hasNext) {
                return false;
            }
            nextKey = StdDraw.nextKeyTyped();
        }

        if (Character.toLowerCase(nextKey) == 'q') {
            saveGame();
            if (server != null) {
                server.stopConnection();
            }
            gameRunning = false;
        }
        return true;
    }

    /**
     * Handles avatar movement.
     */
    private void handleMovement(char key) {
        char lowerKey = Character.toLowerCase(key);
        if (lowerKey == 'w' || lowerKey == 'a'
                || lowerKey == 's' || lowerKey == 'd') {
            if (avatar.move(key)) {
                moveHistory.append(key);
            }
        }
    }

    /**
     * Renders the world and HUD.
     */
    private void renderWorld() {
        ter.renderFrame(world);
        drawHUD();
    }

    /**
     * Draws the Heads Up Display.
     */
    private void drawHUD() {
        StdDraw.setPenColor(Color.DARK_GRAY);
        StdDraw.filledRectangle(WIDTH / 2.0, HEIGHT + HUD_HEIGHT / 2.0,
                WIDTH / 2.0, HUD_HEIGHT / 2.0);

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

        StdDraw.text(WIDTH - 15, HEIGHT + HUD_HEIGHT / 2.0, "Seed: " + seed);
        StdDraw.show();
    }

    /**
     * Returns a description for the given tile.
     */
    private String getTileDescription(TETile tile) {
        if (tile == Tileset.FLOOR) {
            return "floor";
        }
        if (tile == Tileset.WALL) {
            return "wall";
        }
        if (tile == Tileset.AVATAR) {
            return "you";
        }
        if (tile == Tileset.NOTHING) {
            return "nothing";
        }
        if (tile == Tileset.LOCKED_DOOR) {
            return "locked door";
        }
        if (tile == Tileset.UNLOCKED_DOOR) {
            return "unlocked door";
        }
        return "unknown";
    }

    /**
     * Saves the current game state.
     */
    private void saveGame() {
        // Save current avatar position in move history
        String fullHistory = avatar.getX() + "," + avatar.getY() + ";" + moveHistory.toString();
        GameState state = new GameState(seed, fullHistory);
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
     * Loads a saved game state and regenerates the world.
     */
    private void loadAndRestoreGame(String remainingInput) {
        GameState loaded = loadGame();
        if (loaded == null) {
            this.world = createEmptyWorld();
            return;
        }

        this.seed = loaded.getSeed();
        String history = loaded.getMoveHistory();

        // Parse avatar position and move history
        int semicolonIndex = history.indexOf(';');
        if (semicolonIndex > 0) {
            String posStr = history.substring(0, semicolonIndex);
            String moves = history.substring(semicolonIndex + 1);

            // Parse avatar position
            int commaIndex = posStr.indexOf(',');
            int savedX = Integer.parseInt(posStr.substring(0, commaIndex));
            int savedY = Integer.parseInt(posStr.substring(commaIndex + 1));

            // Recreate world from seed
            createWorld(this.seed);

            // Replay moves up to saved position
            this.moveHistory = new StringBuilder();
            for (char c : moves.toCharArray()) {
                if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
                    avatar.move(c);
                    moveHistory.append(c);
                }
            }
        }

        // Process remaining input
        if (remainingInput != null && remainingInput.length() > 0) {
            processInputString(remainingInput);
        }
    }

    /**
     * Loads a saved game state from file.
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
     * Creates an empty world filled with NOTHING tiles.
     */
    private TETile[][] createEmptyWorld() {
        TETile[][] emptyWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                emptyWorld[x][y] = Tileset.NOTHING;
            }
        }
        return emptyWorld;
    }

    /**
     * Method used for autograding and testing your code.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        String lowerInput = input.toLowerCase();

        if (lowerInput.startsWith("l")) {
            // Load game and process remaining input
            loadAndRestoreGame(lowerInput.substring(1));
        } else if (lowerInput.startsWith("n")) {
            int sIndex = lowerInput.indexOf('s', 1);
            if (sIndex == -1) {
                sIndex = lowerInput.length();
            }

            String seedStr = lowerInput.substring(1, sIndex);
            if (seedStr.length() > 0) {
                this.seed = Long.parseLong(seedStr);
                createWorld(this.seed);

                if (sIndex < lowerInput.length()) {
                    processInputString(lowerInput.substring(sIndex + 1));
                }
            } else {
                if (this.world == null) {
                    this.world = createEmptyWorld();
                }
            }
        } else {
            if (this.world == null) {
                this.world = createEmptyWorld();
            }
        }

        return world;
    }

    /**
     * Processes input characters for movement and commands.
     */
    private void processInputString(String input) {
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (c == ':' && i + 1 < input.length() && input.charAt(i + 1) == 'q') {
                saveGame();
                i += 2;
                continue;
            }

            if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
                avatar.move(c);
                moveHistory.append(c);
            }

            i++;
        }
    }

    /**
     * Method used for remote gameplay.
     * @param portStr the port number as a string
     */
    public void interactWithRemoteClient(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            BYOWServer server = new BYOWServer(port);

            gameRunning = true;
            showRemoteMainMenu(server);
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows the main menu to the remote client.
     */
    private void showRemoteMainMenu(BYOWServer server) {
        while (gameRunning) {
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

            server.sendCanvas();

            if (server.clientHasKeyTyped()) {
                char key = Character.toLowerCase(server.clientNextKeyTyped());
                switch (key) {
                    case 'n':
                        handleRemoteNewWorld(server);
                        break;
                    case 'l':
                        handleRemoteLoadWorld(server);
                        break;
                    case 'q':
                        server.stopConnection();
                        gameRunning = false;
                        break;
                    default:
                        break;
                }
            }

            StdDraw.pause(50);
        }
    }

    /**
     * Handles new world creation for remote client.
     */
    private void handleRemoteNewWorld(BYOWServer server) {
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

            server.sendCanvas();

            if (server.clientHasKeyTyped()) {
                char key = server.clientNextKeyTyped();
                if (key == 'S' || key == 's') {
                    enteringSeed = false;
                } else if (Character.isDigit(key)) {
                    seedInput.append(key);
                }
            }

            StdDraw.pause(50);
        }

        if (seedInput.length() > 0) {
            this.seed = Long.parseLong(seedInput.toString());
            createWorld(this.seed);
            startGameLoop(server);
        }
    }

    /**
     * Handles loading a saved world for remote client.
     */
    private void handleRemoteLoadWorld(BYOWServer server) {
        loadAndRestoreGame(null);
        if (this.world != null) {
            startGameLoop(server);
        }
    }
}
