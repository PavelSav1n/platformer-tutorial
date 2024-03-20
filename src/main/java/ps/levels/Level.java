package ps.levels;

import ps.entities.Omon;
import ps.main.Game;
import ps.objects.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.Constants.EnemyConstants.OMON;
import static ps.utils.Constants.ObjectConstants.*;
import static ps.utils.Constants.TEXTURE_MAX_AMOUNT;
//import static ps.utils.HelpMethods.*;

// Level class takes an image and creates all objects lists we need, like OMON, Potions, Containers, Spikes, Cannons.
// It is also determines Player spawn point and offsets for camera to move.
public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Omon> omons = new ArrayList<>(); // OMON are stored in Green RGB pixels.
    private ArrayList<Potion> potions = new ArrayList<>(); // The level stores potions and containers DATA in Blue RGB pixels of level.
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<Grass> grass = new ArrayList<>();
    private ArrayList<AnimatedEnvironment> trees = new ArrayList<>();

    // LVLs can change in width, so we need to calculate xLvlOffset
    private int lvlTilesWide; // number of current level tiles in width
    private int maxTilesOffset; // number of tiles of current level offset (off the screen) in width
    private int maxLvlOffsetX; // number of px of current level offset
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calculateOffsets();
    }

    private void calculateOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    // RED -- is for level data. 58 textures for outside sprites + 2 for water.
    // GREEN -- is for entities (like enemies and player). 2 entities for now.
    // BLUE -- is for objects (creates, potions, cannons, etc.). 10 types for now.
    private void loadLevel() {
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= TEXTURE_MAX_AMOUNT) // Max amount of texture types. Everything over would be 11 (void tile in outside_sprite_atlas).
            lvlData[y][x] = 11;
        else
            lvlData[y][x] = redValue;
        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                    grass.add(new Grass((int) (x * Game.TILES_SIZE), (int) (y * Game.TILES_SIZE) - Game.TILES_SIZE, getRndGrassType(x)));
        }
    }


    private int getRndGrassType(int xPos) {
        return xPos % 2;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case OMON -> omons.add(new Omon(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, BLUE_POTION ->
                    potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT ->
                    cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case TREE, BUSH, FLAG_GEORGIA ->
                    trees.add(new AnimatedEnvironment(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
        }
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public ArrayList<Omon> getOmons() {
        return omons;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public ArrayList<AnimatedEnvironment> getTrees() {
        return trees;
    }

    public ArrayList<Grass> getGrass() {
        return grass;
    }
}
