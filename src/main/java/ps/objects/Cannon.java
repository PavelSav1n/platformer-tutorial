package ps.objects;

import ps.main.Game;


public class Cannon extends GameObject {

    private int tileY;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        hitbox.x -= (int) (4 * Game.SCALE); // Placing image of Cannon in the center of hitbox. 40-6=32
        hitbox.y += (int) (6 * Game.SCALE); // 26+6=32
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }
}
