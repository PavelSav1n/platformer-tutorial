package ps.entities;

import ps.main.Game;

import java.awt.geom.Rectangle2D;

import static ps.utils.Constants.Directions.LEFT;
import static ps.utils.Constants.Directions.RIGHT;
import static ps.utils.Constants.EnemyConstants.*;
import static ps.utils.Constants.*;
import static ps.utils.HelpMethods.*;

public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true; // First update when game starts.
    protected float walkSpeed;
    protected int walkDir = LEFT;
    protected int tileY; // Y position of the tile where the enemy is (needed to check player visibility)
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (firstUpdate)
            if (!isEntityOnFloor(hitbox, lvlData))
                inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) hitbox.y / Game.TILES_SIZE; // when the enemy hits floor it's Y never change
        }
    }

    protected void move(int[][] lvlData) {
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

    // Run towards player
    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        // Checking whether the player on the same Y tile row
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                if (isSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }
        }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x); // Evaluating distance between player and enemy (module of distance is absolute (abs))
        return absValue <= attackDistance * 5; // Eye of sight is 5 times larger than attackDistance
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x); // Evaluating distance between player and enemy (module of distance is absolute (abs))
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState) {
        this.state = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-getEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANI_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, state)) {
                animationIndex = 0;
                switch (state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public boolean isActive() {
        return active;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;

    }
}
