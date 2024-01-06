package ps.main;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    // FPS is for render/frames. Draws the gamescene (the level, player, enemies, etc).
    private final static int FPS_SET = 120; // preferred amount of frames per sec.
    // UPS is for update/tick. Takes care of logic (playermove, events, etc)
    private final static int UPS_SET = 200; // preferred amount of updates  per sec

    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true); // needed for keyboard input to work
        gamePanel.requestFocusInWindow(); // to pass key pressing to this GUI window
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET; // the duration that each frame should last in nanosec
        double timePerUpdate = 1000000000.0 / UPS_SET; // frequency of updates
        long lastFrame = System.nanoTime(); // time when we last checked our Frame
        long now;

        long previousTime = System.nanoTime();

        int frames = 0; // Counter for FPS.
        int updates = 0; // Counter for UPS.
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;

        // Endless loop where we every 120 frames repainting gamePanel and once per sec printing current FPS
        while (true) {
            now = System.nanoTime(); // Each cycle we write current time in this variable
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate; // deltaU is for


            if (now - lastFrame >= timePerFrame) { // if now time minus the last time we've checked is >= the time we wish one frame to last. Whenever it's ">" it means we've lost some frames
                gamePanel.repaint(); // Here can be a loss of frames, because it takes some time to repaint.
                lastFrame = now;
                frames++;
            }

            // Once per sec printing the value of frames counter.
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }


    }
}
