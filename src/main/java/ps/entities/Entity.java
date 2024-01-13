package ps.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x, y; // class that extends this class can use these dimensions.
    protected int width, height;
    protected Rectangle2D.Float hitbox; // Float is to use float values in hitbox.

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

    protected void initHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    // Get new x & y and update hitbox
//    protected void updateHitbox() {
//        hitbox.x = (int) x;
//        hitbox.y = (int) y;
//    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
