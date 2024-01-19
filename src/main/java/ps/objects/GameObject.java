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
                if (objType == BARREL || objType == BOX) { // If it is Barrel or Box, stop doing animation and it becomes !active.
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset() {
        System.out.println(objType + "RESET!");
        animationTick = 0;
        animationIndex = 0;
        active = true;
        if (objType == BARREL || objType == BOX)
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

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}
