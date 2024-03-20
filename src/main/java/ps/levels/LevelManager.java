package ps.levels;

import ps.gamestates.Gamestate;
import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.Constants.ObjectConstants.*;
import static ps.utils.Constants.TEXTURE_MAX_AMOUNT;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private BufferedImage[] waterSprite;
    private BufferedImage borderPost, signLars;
    private ArrayList<Level> levels;
    private int lvlIndex = 0, aniTick, aniIndex;
    ;

    public LevelManager(Game game) {
        this.game = game;
//        levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        createWater();
        this.borderPost = LoadSave.GetSpriteAtlas(LoadSave.BORDER_POST); // Loading BorderPost image.
        this.signLars = LoadSave.GetSpriteAtlas(LoadSave.SIGN_LARS); // Loading Lars sign image.
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
        for (int i = 0; i < 4; i++)
            waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
        waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
    }


    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new Level(img));
        }
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[TEXTURE_MAX_AMOUNT];
        // Nested loop is for easier cutting atlas into 1 dimensional array. 5 rows, 12 columns
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                int x = Game.TILES_SIZE * i - lvlOffset;
                int y = Game.TILES_SIZE * j;
                if (index == 58)
                    g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else if (index == 59)
                    g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else if (index == 60)
                    g.drawImage(borderPost, x, y - BORDER_POST_HEIGHT + (int) (Game.SCALE * Game.TILES_DEFAULT_SIZE), BORDER_POST_WIDTH, BORDER_POST_HEIGHT, null);
                else if (index == 61)
                    g.drawImage(signLars, x, y - SIGN_LARS_HEIGHT + (int) (Game.SCALE * Game.TILES_DEFAULT_SIZE), SIGN_LARS_WIDTH, SIGN_LARS_HEIGHT, null);
                else
                    g.drawImage(levelSprite[index], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);

            }
    }


    public void update() {
        updateWaterAnimation();
    }

    private void updateWaterAnimation() {
        aniTick++;
        if (aniTick >= 40) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= 4)
                aniIndex = 0;
        }
    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("ALL LEVELS COMPLETED!");
            Gamestate.state = Gamestate.MENU;
        }
        // If there are more lvls:
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffsetX(newLevel.getLvlOffsetX());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    // For State class to choose what song to play.
    public int getLvlIndex() {
        return lvlIndex;
    }
}
