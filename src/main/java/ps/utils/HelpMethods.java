package ps.utils;

import ps.main.Game;

public class HelpMethods {

    // This method checks whether all 4 passed points (x,y,x+width,y+height) interact with solid data in lvlData
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // First checking top left and bottom right. If ok, then top right & bottom left.
        if (!isSolid(x, y, lvlData))
            if (!isSolid(x + width, y + height, lvlData))
                if (!isSolid(x + width, y, lvlData))
                    if (!isSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    // Defining what solid data is:
    private static boolean isSolid(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= Game.GAME_WIDTH) return true; // If we are in borders of the Game itself by x & y.
        if (y < 0 || y >= Game.GAME_HEIGHT) return true;

        float xIndex = x / Game.TILES_SIZE; // Getting x index scaled down by TITLE_SIZE (for example if x is at 65, when TS is 64, it means we're at 2nd column of the lvl)
        float yIndex = y / Game.TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (value >= 48 || value < 0 || value != 11)
            return true; // We have only 48 tiles, so checking that and 11th tile is void tile, so we can pass through.
        return false;
    }
}
