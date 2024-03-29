package ps.main;

import ps.audio.AudioPlayer;
import ps.gamestates.GameOptions;
import ps.gamestates.Gamestate;
import ps.gamestates.Menu;
import ps.gamestates.Playing;
import ps.ui.AudioOptions;
import ps.utils.LoadSave;

import java.awt.*;

// This is where:
// 1. Initializing all entities for the game
// 2. Initializing GamePanel & GameWindow
// 3. Where our game loop runs
// 4. Where updating and rendering going on.
public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    // FPS is for render/frames. Draws the gamescene (the level, player, enemies, etc).
    private final static int FPS_SET = 120; // preferred amount of frames per sec.
    // UPS is for update/tick. Takes care of logic (playermove, events, etc.)
    private final static int UPS_SET = 200; // preferred amount of updates  per sec

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    // Scaling ang dimensions:
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2.0f;
    public final static int TILES_IN_WIDTH = 26; // visible tiles on screen (26 default)
    public final static int TILES_IN_HEIGHT = 14; // visible tiles on screen (14 default)
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


    public Game() {

        initClasses(); // initialize all entities needed for a game.
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true); // needed for keyboard input to work
        gamePanel.requestFocusInWindow(); // to pass key pressing to this GUI window

        startGameLoop();
    }

    // Instantiations of classes:
    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (Gamestate.state) {
            case PLAYING -> playing.update();
            case MENU -> menu.update();
            case OPTIONS -> gameOptions.update();
            case QUIT -> System.exit(0);


        }
    }


    public void render(Graphics graphics) {
        switch (Gamestate.state) {
            case PLAYING -> playing.draw(graphics);
            case MENU -> menu.draw(graphics);
            case OPTIONS -> gameOptions.draw(graphics);
            case QUIT -> System.exit(0);

        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET; // the duration that each frame should last in nanosec
        double timePerUpdate = 1000000000.0 / UPS_SET; // frequency of updates

        long previousTime = System.nanoTime();

        int frames = 0; // Counter for FPS.
        int updates = 0; // Counter for UPS.
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0; // delta for updates
        double deltaF = 0; // delta for frames

        // Endless loop where we every 120 frames repainting gamePanel and once per sec printing current FPS
        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate; // deltaU is for storing updates loss and to know when to update the game
            deltaF += (currentTime - previousTime) / timePerFrame; // deltaU is for storing updates loss and to know when to update the game
            previousTime = currentTime;

            if (deltaU >= 1) { // if deltaU sum is equal or passing over threshold "1" we need to update
                update();
                updates++;
                deltaU--; // deltaU never loses a tick, because it is decremented exactly by 1 and all decimals are stored
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // Once per sec printing the value of frames counter.
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: " + frames + " | UPS: " + updates);
                updates = 0;
                frames = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirectionBooleans();
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
