package ps.entities;

import ps.main.Game;

import static ps.utils.Constants.UI.EnemyConstants.*;

public class Crabby extends Enemy {


    public Crabby(float x, float y) { // all other constants are defined already
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE)); // 22 and 19 is measured crabby hitbox.
    }

}

