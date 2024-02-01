package ps.objects;

import ps.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ps.utils.Constants.ANI_SPEED;
import static ps.utils.Constants.ObjectConstants.*;

public class GameObject {

    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset; // These offsets are the distance from edges of sprite grid (for example potion offset is 3 for X and 2 for Y)

    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }


    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANI_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(objType)) {
                animationIndex = 0;
                if (objType == BARREL || objType == BOX) { // If it is Barrel or Box, stop doing animation and it becomes !active. If we are here, it means we've broken a container, and it's started an animation
                    doAnimation = false;
                    active = false;
                } else if (objType == CANNON_LEFT || objType == CANNON_RIGHT) { // We're doing animation once and then checking again is a player in the line of sight.
                    doAnimation = false;
                }
            }
        }
    }

    public void reset() {
        animationTick = 0;
        animationIndex = 0;
        active = true;
        if (objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
            doAnimation = false;
        else
            doAnimation = true;

    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public void drawHitbox(Graphics graphics, int xLvlOffset) {
        // For debugging the hitbox
        graphics.setColor(Color.PINK);
        graphics.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public int getObjType() {
        return objType;
    }


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }


    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}
