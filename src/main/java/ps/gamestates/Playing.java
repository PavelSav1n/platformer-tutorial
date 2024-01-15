package ps.gamestates;

import ps.entities.EnemyManager;
import ps.entities.Player;
import ps.levels.LevelManager;
import ps.main.Game;
import ps.ui.PauseOverlay;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static ps.utils.Constants.UI.Environment.*;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;

    private int xLvlOffset; // will be applied when player reach left or right offset border
    private final int leftBorder = (int) (0.45 * Game.GAME_WIDTH); // 20% of game_width
    private final int rightBorder = (int) (0.55 * Game.GAME_WIDTH);
    private final int lvlTilesWide = LoadSave.GetLevelData()[0].length; // number of current level tiles in width
    private final int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH; // number of tiles of current level offset (off the screen) in width
    private final int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE; // number of px of current level offset

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos; // will store Y values of small clouds to place them randomly.
    private Random rnd = new Random();

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE)); // we got here from 90 to 100 at least.
        }
    }

    // Initializing
    // 1. LevelManager
    // 2. Player
    // 3. LevelData
    // 4. Pause Overlay
    // 5. Enemies
    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(250, 250, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);

    }


    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player); // To manage lvl data like solid blocks and cliffs inside Enemy class (updateMove() method)
            player.update();
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(graphics);

        levelManager.draw(graphics, xLvlOffset);
        enemyManager.draw(graphics, xLvlOffset);
        player.render(graphics, xLvlOffset);

        if (paused) {
            graphics.setColor(new Color(0, 0, 0, 150)); // Semi transparent black.
            graphics.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT); // Filling rect in bg to darken gameplay while paused.
            pauseOverlay.draw(graphics);
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.2), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.3), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }

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

    public void mouseDragged(MouseEvent mouseEvent) {
        if (paused)
            pauseOverlay.mouseDragged(mouseEvent);
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

            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
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

    public void unpauseGame() {
        paused = false;
    }
}
