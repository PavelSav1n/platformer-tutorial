package ps.main;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final static int FPS_SET = 120; // preferred amount of frames

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
        long lastFrame = System.nanoTime(); // time when we last checked our Frame
        long now;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {
            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame) { // if now time minus the last time we've checked is >= the time we wish one frame to last
                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }


            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }


    }
}
