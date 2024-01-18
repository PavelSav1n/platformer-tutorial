package ps.levels;

import ps.entities.Crabby;
import ps.main.Game;
import ps.objects.GameContainer;
import ps.objects.Potion;
import ps.utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.HelpMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs; // Crabs are stored in Green RGB pixels.
    private ArrayList<Potion> potions; // The level stores potions and containers DATA in Blue RGB pixels of level.
    private ArrayList<GameContainer> containers;
    // LVLs can change in width, so we need to calculate xLvlOffset
    private int lvlTilesWide; // number of current level tiles in width
    private int maxTilesOffset; // number of tiles of current level offset (off the screen) in width
    private int maxLvlOffsetX; // number of px of current level offset
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        calculateOffsets();
        calculatePlayerSpawn();
    }

    private void createPotions() { // Fill the list with potions with the specified coordinates according to passing img.
        potions = HelpMethods.GetPotions(img);
    }

    private void createContainers() { // Fill the list with game containers with the specified coordinates according to passing img.
        containers = HelpMethods.GetContainers(img);
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calculateOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
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

    public ArrayList<Crabby> getCrabs() {
        return crabs;
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
}
