package ps.objects;

import ps.main.Game;

import java.awt.geom.Rectangle2D;

import static ps.utils.Constants.ANI_SPEED;
import static ps.utils.Constants.GRAVITY;
import static ps.utils.Constants.ObjectConstants.*;
import static ps.utils.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir; // Two ways
    private boolean active = true;
    private int animationTick, animationIndex;
    private float throwSpeed = -1.25f * Game.SCALE;

    public Projectile(int x, int y, int dir, int projectileType) {
        switch (projectileType) {
            case CANNON_BALL -> {
                int xOffset = (int) (-3 * Game.SCALE);
                int yOffset = (int) (5 * Game.SCALE);
                if (dir == 1)
                    xOffset = (int) (29 * Game.SCALE);
                this.hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
            }
            case CUP -> {
                int xOffset = (int) (-3 * Game.SCALE);
                int yOffset = (int) (5 * Game.SCALE);
                if (dir == 1)
                    xOffset = (int) (12 * Game.SCALE);
                this.hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CUP_WIDTH, CUP_HEIGHT);
            }
        }

        this.dir = dir;
    }

    public void updatePos(int projectileType) {
        switch (projectileType) {
            case CANNON_BALL -> {
                hitbox.x += dir * SPEED;
            }
            case CUP -> {
                hitbox.y += throwSpeed;
                throwSpeed += GRAVITY / 2;

                hitbox.x += dir * SPEED;
            }
        }
    }

    // in case of resetting a projectile
    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    public void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANI_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(CUP)) {
                animationIndex = 0;
            }
        }
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}
