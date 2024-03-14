package ps.utils;

//import ps.entities.Crabby;

import ps.main.Game;
import ps.objects.Projectile;

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

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
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

    public static boolean IsProjectileHittingLevel(Projectile projectile, int[][] lvlData) {
        return isSolid(projectile.getHitbox().x + projectile.getHitbox().width / 2, projectile.getHitbox().y + projectile.getHitbox().height / 2, lvlData); // need to add some offsets;
    }

    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Will only check if entity touch top water. Can't reach bottom water if not
        // touched top water.
        if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
            if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
                return false;
        return true;
    }

    private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / Game.TILES_SIZE);
        int yCord = (int) (yPos / Game.TILES_SIZE);
        return lvlData[yCord][xCord];
    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        // We have only 49 tiles, so checking that and 11th tile is void tile, so we can pass through.
        switch (value) {
            case 11, 48, 49 -> {
                return false;
            }
            default -> {
                return true;
            }
        }
    }

    public static boolean canCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        // We need to check only those tile which in between firstXTile and secondXTile
        if (firstXTile > secondXTile)  // Going through tile from left to right, so we need this check
            return isAllTilesClear(secondXTile, firstXTile, tileY, lvlData);
        else
            return isAllTilesClear(firstXTile, secondXTile, tileY, lvlData);

    }

    // If all tiles from 1 point to 2 point is not solid
    public static boolean isAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (isTileSolid(xStart + i, y, lvlData))
                return false;
        return true;

    }

    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isAllTilesClear(xStart, xEnd, y, lvlData))
                if (!isTileSolid(xStart + i, y + 1, lvlData)) // checking tiles beneath clear tiles
                    return false;
        }
        return true;

    }

    // Checking whether there is an obstacle in sight between two objects.
    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
        int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

        int secondXTile;
        if (isSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
            secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
        else
            secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return isAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return isAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }
}

