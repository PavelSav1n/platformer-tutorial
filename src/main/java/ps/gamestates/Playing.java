package ps.gamestates;

import ps.entities.Player;
import ps.levels.LevelManager;
import ps.main.Game;
import ps.ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = true;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    // Initializing
    // 1. LevelManager
    // 2. Player
    // 3. LevelData
    // 4. Pause Overlay
    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(250, 250, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay();
    }


    @Override
    public void update() {
        levelManager.update();
        player.update();
        pauseOverlay.update();
    }

    @Override
    public void draw(Graphics graphics) {
        levelManager.draw(graphics);
        player.render(graphics);
        pauseOverlay.draw(graphics);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (paused)
            pauseOverlay.mousePressed(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (paused)
            pauseOverlay.mouseReleased(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (paused)
            pauseOverlay.mouseMoved(mouseEvent);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setUp(true);
//                System.out.println("w");
                break;
            case KeyEvent.VK_A:
                player.setLeft(true);
//                System.out.println("a");
                break;
            case KeyEvent.VK_S:
                player.setDown(true);
//                System.out.println("s");
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
//                System.out.println("d");
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setUp(false);
                break;
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_S:
                player.setDown(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }

    }

    public Player getPlayer() {
        return player;
    }


    public void windowFocusLost() {
        player.resetDirectionBooleans();
    }
}
