package ps.gamestates;

import ps.entities.EnemyManager;
import ps.entities.Player;
import ps.levels.LevelManager;
import ps.main.Game;
import ps.objects.ObjectManager;
import ps.ui.GameOverOverlay;
import ps.ui.LevelCompletedOverlay;
import ps.ui.PauseOverlay;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static ps.utils.Constants.Environment.*;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private ObjectManager objectManager;

    private boolean paused = false;

    private int xLvlOffset; // will be applied when player reach left or right offset border
    private final int leftBorder = (int) (0.45 * Game.GAME_WIDTH); // 20% of game_width
    private final int rightBorder = (int) (0.55 * Game.GAME_WIDTH);
    private int maxLvlOffsetX; // number of px of current level offset

    private BufferedImage backgroundImg, bigCloud, smallCloud, mountain;
    private int[] smallCloudsPos; // will store Y values of small clouds to place them randomly.
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean levelCompleted;
    private boolean playerDying = false;


    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        mountain = LoadSave.GetSpriteAtlas(LoadSave.MOUNTAIN);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (200 * Game.SCALE)); // we got here from 50 to 100 at least.
        }
        calcLvlOffset();
        loadStartLevel();
    }

    public void setMaxLvlOffsetX(int maxLvlOffsetX) {
        this.maxLvlOffsetX = maxLvlOffsetX;
    }


    public void loadNextLevel() {
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
        resetAll(); // Resetting atatckBox and stuff after setting a Player.
    }

    // Initialising enemies and objects of the game.
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
        resetAll(); // Added due to attackBox misplacing at the start.
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffsetX();
    }

    // Needed for GAME OVER in the end of lvl
    public float totalLvlWidth() {
        return levelManager.getCurrentLevel().getLevelData()[0].length * Game.TILES_DEFAULT_SIZE * Game.SCALE;
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
        objectManager = new ObjectManager(this);

        player = new Player(250, 250, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);

    }


    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            enemyManager.update(levelManager.getCurrentLevel().getLevelData()); // To manage lvl data like solid blocks and cliffs inside Enemy class (updateMove() method)
            player.update();
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        }
        if (xLvlOffset < 0)
            xLvlOffset = 0;

        // Completing level when reaching its right border:
        if (player.getHitbox().x >= totalLvlWidth() - player.getHitbox().width - 1) {
            setLevelCompleted(true);
        }

    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawMountains(graphics);
        drawClouds(graphics);

        levelManager.draw(graphics, xLvlOffset);
        player.render(graphics, xLvlOffset);
        enemyManager.draw(graphics, xLvlOffset);
        objectManager.draw(graphics, xLvlOffset);
        objectManager.drawBackgroundTrees(graphics, xLvlOffset);

        if (paused) {
            graphics.setColor(new Color(0, 0, 0, 150)); // Semi transparent black.
            graphics.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT); // Filling rect in bg to darken gameplay while paused.
            pauseOverlay.draw(graphics);
        } else if (gameOver) {
            gameOverOverlay.draw(graphics);
        } else if (levelCompleted) {
            levelCompletedOverlay.draw(graphics);
        }

    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.2), smallCloudsPos[i] + 150, BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }

        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.3), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    private void drawMountains(Graphics g) {
        for (int i = 0; i < 5; i++) {
            g.drawImage(mountain, i * MOUNTAIN_WIDTH - (int) (xLvlOffset * 0.1), (int) (204 * Game.SCALE), MOUNTAIN_WIDTH, MOUNTAIN_HEIGHT, null);
        }
    }

    public void resetAll() {
        //reset enemy, player, everything...
        gameOver = false;
        paused = false;
        levelCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public int checkEnemyHit(Rectangle2D.Float attackBox) {
        return enemyManager.checkEnemyHit(attackBox);
    }

    public int checkEnemyHitWithCup(Rectangle2D.Float attackBox) {
        return enemyManager.checkEnemyHitWithCup(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
//        if (!gameOver)
//            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
//                player.setAttacking(true);
//            }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (!gameOver) {
            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                player.powerAttack();
            } else if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                player.setThrowingCups(true);
            }
            if (paused)
                pauseOverlay.mousePressed(mouseEvent);
            else if (levelCompleted) {
                levelCompletedOverlay.mousePressed(mouseEvent);
            }
        } else
            gameOverOverlay.mousePressed(mouseEvent);

    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(mouseEvent);
            else if (levelCompleted) {
                levelCompletedOverlay.mouseReleased(mouseEvent);
            }
        } else
            gameOverOverlay.mouseReleased(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(mouseEvent);
            else if (levelCompleted) {
                levelCompletedOverlay.mouseMoved(mouseEvent);
            }
        } else
            gameOverOverlay.mouseMoved(mouseEvent);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (gameOver)
            gameOverOverlay.keyPressed(keyEvent);
        else
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(true);
                case KeyEvent.VK_D -> player.setRight(true);
                case KeyEvent.VK_SPACE -> player.setJump(true);
                case KeyEvent.VK_ESCAPE -> paused = !paused;
            }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (!gameOver)
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_SPACE -> player.setJump(false);
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;

    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
        if (levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }


    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    public int getMaxLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public int getxLvlOffset() {
        return xLvlOffset;
    }
}
