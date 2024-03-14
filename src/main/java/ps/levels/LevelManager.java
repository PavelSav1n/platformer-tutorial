package ps.levels;

import ps.gamestates.Gamestate;
import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private BufferedImage[] waterSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0, aniTick, aniIndex;
    ;

    public LevelManager(Game game) {
        this.game = game;
//        levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        createWater();
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
        levelSprite = new BufferedImage[48];
        // Nested loop is for easier cutting atlas into 1 dimensional array. 4 rows, 12 columns
        for (int j = 0; j < 4; j++) {
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
                if (index == 48)
                    g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else if (index == 49)
                    g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
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
