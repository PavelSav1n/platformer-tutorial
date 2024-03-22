package ps.entities;

import ps.gamestates.Playing;

import java.util.Random;

import static ps.utils.Constants.Dialogue.DIALOGUE_ATTACK;
import static ps.utils.Constants.Dialogue.DIALOGUE_DEATH;
import static ps.utils.Constants.EnemyConstants.*;
import static ps.utils.HelpMethods.isFloor;

public class Omon extends Enemy {

    int r = new Random().nextInt(4);

    public Omon(float x, float y) { // all other constants are defined already
        super(x, y, OMON_WIDTH, OMON_HEIGHT, OMON);
        initHitbox(25, 27); // 22 and 19 is measured OMON hitbox.
        initAttackBox(15, 27, 15);  // 30 on the left side + 30 on the right and 22 in the middle
    }

    private void updateBehavior(int[][] lvlData, Playing playing) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir)
            inAirChecks(lvlData, playing);
        else {
            switch (state) {
                case IDLE -> {
                    if (isFloor(hitbox, lvlData))
                        newState(RUNNING); // enemyState becomes equal passed enum and aniTick & aniIndex become 0;
                    else
                        inAir = true;
                }
                case RUNNING -> {
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        if (!speaked && r == 1) { // speaking with 25% possibility
                            playing.addDialogue((int) hitbox.x, (int) hitbox.y, DIALOGUE_ATTACK);
                            speaked = true;
                        }
                        if (isPlayerCloseForAttack(playing.getPlayer()))
                            newState(ATTACK);
                    }
                    move(lvlData);
                }
                case ATTACK -> {
                    if (animationIndex == 0) attackChecked = false; // if animation is passed, we can check again.
                    if (animationIndex == 1 && !attackChecked) // attackChecked for only one hit checking per ATTACK
                        checkPlayerHit(attackBox, playing.getPlayer());
                }
                case HIT -> {
                    if (animationIndex <= getSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 0.5f);
                    updatePushBackDrawOffset();
                }
                case DEAD -> {
                    if (!speaked && r == 2) { // speaking with 25% possibility
                        playing.addDialogue((int) hitbox.x, (int) hitbox.y, DIALOGUE_DEATH);
                        speaked = true; // Avoiding stupid ConcurrentModificationException
                    }
                }
            }
        }
    }

    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
//        updateAttackBox(); // In case we need attackBox be on both sides
        updateAttackBoxFlip();
    }
}

