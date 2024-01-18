package ps.entities;

import ps.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x, y; // class that extends this class can use these dimensions.
    protected int width, height;
    protected Rectangle2D.Float hitbox; // Float is to use float values in hitbox.
    protected int animationTick, animationIndex; // for running through massive of sprites with determined speed
    protected int state;
    protected float airSpeed; // Speed at what entity is travelling through the air (jumping or falling)
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected float walkSpeed = 1.0f * Game.SCALE;

    //AttackBox
    protected Rectangle2D.Float attackBox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics graphics, int xLvlOffset) {
        // For debugging the hitbox
        graphics.setColor(Color.PINK);
        graphics.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - lvlOffsetX), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    // Get new x & y and update hitbox
//    protected void updateHitbox() {
//        hitbox.x = (int) x;
//        hitbox.y = (int) y;
//    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getState() {
        return state;
    }

    public int getAniIndex() {
        return animationIndex;
    }

}
