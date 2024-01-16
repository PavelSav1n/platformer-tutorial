package ps.entities;

import ps.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ps.utils.Constants.Directions.LEFT;
import static ps.utils.Constants.Directions.RIGHT;
import static ps.utils.Constants.UI.EnemyConstants.*;
import static ps.utils.HelpMethods.*;

public class Crabby extends Enemy {

    //AttackBox
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y) { // all other constants are defined already
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE)); // 22 and 19 is measured crabby hitbox.
        initAttackBox();
    }

    // Offset is needed to center attack box relative to hitbox.
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 30); // 30 on the left side + 30 on the right and 22 in the middle
    }


    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING); // enemyState becomes equal passed enum and aniTick & aniIndex become 0;
                case RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                }
                case ATTACK -> {
                    if (aniIndex == 0) attackChecked = false; // if animation is passed, we can check again.
                    if (aniIndex == 3 && !attackChecked) // attackChecked for only one hit checking per ATTACK
                        checkEnemyHit(attackBox, player);
                }
                case HIT -> {
                }
            }
        }

    }


    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    // We're flipping X because we need to keep sprite centered, so X from topleft goes to topright on the distance of width of sprite.
    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else return 1;
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

}

