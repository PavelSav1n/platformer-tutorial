package ps.utils;

import ps.main.Game;

import java.awt.geom.Rectangle2D;

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


    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {

        int previousTileNum = (int) (hitbox.x / Game.TILES_SIZE); // # of previous tile

        if (xSpeed > 0) {
            // Right
            int currentTileXPos = previousTileNum * Game.TILES_SIZE; // x position in px where current tile starts. (Graphic starts at 0xp).
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width); // Offset from left side of tile.
            return currentTileXPos + xOffset - 1; // -1 because grid px starts in 0. 0-1-2-3-4
        } else {
            // Left
            return previousTileNum * Game.TILES_SIZE;
        }
    }

    // This method returns an Y px exactly at the beginning of tile or at the [end_of_tile - hitbox.height - 1].
    // Hitbox must be smaller than tile size.
    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int previousTileNum = (int) (hitbox.y / Game.TILES_SIZE);

        if (airSpeed > 0) {
            // Falling -- touching floor
            int currentTileYPos = previousTileNum * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return currentTileYPos + yOffset - 1;
        } else {
            // Jumping
            return previousTileNum * Game.TILES_SIZE;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottomleft & bottomright corners of hitbox.
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            return isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        }
        return true;
    }

    // Checking if the hitbox.x + xSpeed pixel isSolid data of lvlData (actually we're not checking pixel hitbox.x + xSpeed + hitbox.width on our right side)
    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    // Defining what solid data is:
    private static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth) return true; // If we are in borders of the Game itself by x & y.
        if (y < 0 || y >= Game.GAME_HEIGHT) return true;

        float xIndex = x / Game.TILES_SIZE; // Getting x index scaled down by TITLE_SIZE (for example if x is at 65, when TS is 64, it means we're at 2nd column of the lvl)
        float yIndex = y / Game.TILES_SIZE;

        return isTileSolid((int) xIndex, (int) yIndex, lvlData);

    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        if (value >= 48 || value < 0 || value != 11)
            return true; // We have only 48 tiles, so checking that and 11th tile is void tile, so we can pass through.
        return false;
    }

    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + i, y, lvlData))
                return false;
            if (!isTileSolid(xStart + i, y+1, lvlData))
                return false;
        }
        return true;

    }

    // Checking whether there is an obstacle in sight between two objects.
    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        // We need to check only those tile which in between firstXTile and secondXTile
        if (firstXTile > secondXTile)  // Going through tile from left to right, so we need this check
            return isAllTilesWalkable(secondXTile, firstXTile, tileY, lvlData);
        else
            return isAllTilesWalkable(firstXTile, secondXTile, tileY, lvlData);


    }
}

