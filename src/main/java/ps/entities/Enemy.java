package ps.entities;

import ps.main.Game;

import static ps.utils.Constants.Directions.*;
import static ps.utils.Constants.UI.EnemyConstants.*;
import static ps.utils.HelpMethods.*;

public abstract class Enemy extends Entity {

    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;
    private boolean firstUpdate = true; // First update when game starts.
    private boolean inAir = false;
    private float fallSpeed;
    private float gravity = 0.04f * Game.SCALE;
    private float walkSpeed = 1.0f * Game.SCALE;
    private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }
        }
    }

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            if (!isEntityOnFloor(hitbox, lvlData))
                inAir = true;
            firstUpdate = false;
        }

        if (inAir) {
            if (canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE -> enemyState = RUNNING;
                case RUNNING -> {
                    float xSpeed = 0;
                    if (walkDir == LEFT)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;

                    if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                         if (isFloor(hitbox, xSpeed, lvlData)) {

                             // if I want enemies to fall of the cliff
//                        if (!isEntityOnFloor(hitbox, lvlData)) {
//                            inAir = true;
//                        } else {

                            hitbox.x += xSpeed;
                            return; // return from this case switch
                        }
                    }
                    changeWalkDir(); // if there is no floor, changing direction
                }
            }
        }

    }

    private void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
