package ps.entities;

import ps.main.Game;

import static ps.utils.Constants.Directions.LEFT;
import static ps.utils.Constants.UI.EnemyConstants.*;
import static ps.utils.HelpMethods.*;

public class Crabby extends Enemy {


    public Crabby(float x, float y) { // all other constants are defined already
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE)); // 22 and 19 is measured crabby hitbox.
    }


    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING); // enemyState becomes equal passed enum and aniTick & aniIndex become 0;
                case RUNNING -> {
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                }
            }
        }

    }


    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

}

