package ps.entities;

import ps.audio.AudioPlayer;
import ps.gamestates.Playing;
import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static ps.utils.Constants.ANI_SPEED;
import static ps.utils.Constants.GRAVITY;
import static ps.utils.Constants.PlayerConstants.*;
import static ps.utils.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;

    private int playerDirection = -1; // Moving state. If not moving =-1, otherwise it's 0, 1, 2 or 3
    private boolean moving = false;
    private boolean attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData; // We are storing lvl data in player class just for now. We need it to detect collision.
    private float xDrawOffset = 21 * Game.SCALE; // Offset where the new hitbox starts (not 0x0 but 21x4). A player drawing will use this.
    private float yDrawOffset = 4 * Game.SCALE; // Sprite of the player is a bit more than hitbox, so we need some offsets to center sprite

    // Jumping / Gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
//    private boolean inAir = false;

    // StatusBarUI
    private BufferedImage statusBarImg;
    // BG for healthBar and powerBar:
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);
    // healthBar
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int healthWidth = healthBarWidth;
    // powerBar
    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0; // to check Y tile (line of sight check).

    // Power Attack.
    private boolean powerAttackActive;
    private int powerAttackTick; // Duration of power attack in ticks.
    private int powerGrowSpeed = 15; // The amount of ticks it takes to regenerate some power.
    private int powerGrowTick;


    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = 35;
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations();
        initHitbox(20, 27); // Initializing & Drawing hitbox with a size of 20x27 at x, y.
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    public void update() {
        updateHealthBar(); // updating healthbar and then checking weather game is over.
        updatePowerBar();

        // Die mechanics
        if (currentHealth <= 0) {
            if (state != DEAD) {
                state = DEAD;
                animationTick = 0;
                animationIndex = 0;
                playing.setPlayerDying(true); // stopping everything but player animation of death.
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE); // Play death sound effect.
            } else if (animationIndex == getSpriteAmount(DEAD) - 1 && animationTick >= ANI_SPEED - 1) { // -1 because index starts with 0.
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong(); // Stop playing song.
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER); // Start playing game over effect.
            } else
                updateAnimationTick();
            return; // this needed to not go through the rest of the code below
        }

        updateAttackBox();

        updatePosition(); // if moving updating position
        if (moving) {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE); // updating tileY
            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) { // Duration of powerAttack animation.
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if (attacking || powerAttackActive)
            checkAttack();
//        updateHitbox(); //
        updateAnimationTick();
        setAnimation(); // to set proper playerAction
    }


    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1) // If attack animation is in motion we should not be here.
            return;
        attackChecked = true;

        if (powerAttackActive)
            attackChecked = false; // To skip attack check and to hit targets with each tick.

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    // Updating attackBox placement according to movement direction (left/right or powerAttack with flipW direction)
    private void updateAttackBox() {
        if (right || (powerAttackActive && flipW == 1)) {
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
        } else if (left || (powerAttackActive && flipW == -1)) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (int) (Game.SCALE * 10);
    }

    private void updatePowerBar() {
        powerWidth = (int) (powerValue / (float) powerMaxValue * powerBarWidth); // If powerValue shrinks then powerWidth will shrink.
        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1); // The amount of power to regenerate per tick.
        }
    }

    private void updateHealthBar() {
        healthWidth = (int) (currentHealth / (float) maxHealth * healthBarWidth); // To match healthWidth to current numeric number.
    }

    public void render(Graphics graphics, int lvlOffset) {
        // getting image and drawing width x height (64x40 -- default) part at animations[i][j] of initial image
        // hitbox.x -xDrawOffset is where to draw a player.
        // We're drawing a player after drawing a hitbox, with a bit of offset.
        graphics.drawImage(
                animations[state][animationIndex],
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset),
                width * flipW, height, null);
        drawHitbox(graphics, lvlOffset);
        drawAttackBox(graphics, lvlOffset);
        drawUI(graphics);
    }


    private void drawUI(Graphics g) {
        // Background UI
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health bar
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        // Power bar
        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight); // powerWidth not powerBarWidth to make it able to shrink or expand according to current values
    }

    // Updating position so, that we can hold two keys and run diagonally or stop moving at all.
    // Also checking, whether it's possible to go in chosen direction.
    // Moving now depends on hitbox of a player (20x28), not a player original sprite size (64x40).
    private void updatePosition() {
        moving = false; // false by default

        if (jump) {
            jump();
        }
        // If we're pressing A+D or not A+D, and we're not in the powerAttack motion -- we should not be here
        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left)) return;

        float xSpeed = 0; // Speed is by default 0.

        if (left) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttackActive) {
            if (!left && !right) { // If we're not going left or right.
                if (flipW == -1) // If the last time we moved, we were going on the left.
                    xSpeed = -walkSpeed; // To make xSpeed not 0 but some value.
                else
                    xSpeed = walkSpeed;
            }
            xSpeed *= 3; // Multiplying xSpeed anyway.
        }

        if (!inAir) {
            if (!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir && !powerAttackActive) {
            if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPosition(xSpeed);
            } else {
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) // If we are going down.
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPosition(xSpeed);

            }

        } else
            updateXPosition(xSpeed);
        moving = true;
    }


    private void jump() {
        if (inAir) return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }


    private void updateXPosition(float xSpeed) {
        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else { // If we cannot move further
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed); // Stick player to the solid tile.
            if (powerAttackActive) { // Break power attack
                powerAttackActive = false;
                powerAttackTick = 0; // Not to store ticks for the next power attack.
            }
        }
    }

    // Method, that not allow health to exceed limits (<0 or >100)
    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0) {
            currentHealth = 0;
            // TODO: gameOver();
        } else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }


    public void kill() {
        currentHealth = 0;
    }

    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue)
            powerValue = powerMaxValue;
        else if (powerValue <= 0)
            powerValue = 0;
    }

    private void setAnimation() {
        int startAnimation = state; // Remember what current animation is before checking is there a switch.

        if (moving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0)
                state = JUMPING;
            else
                state = FALLING;
        }

        // Will be the same animation throughout entire power attack.
        if (powerAttackActive) {
            state = ATTACK;
            animationIndex = 1;
            animationTick = 0;
            return;
        }

        if (attacking) {
            state = ATTACK;
            if (startAnimation != ATTACK) { // Shortening animation for faster response on mouse click.
                animationIndex = 1; // starting skipping first sprite.
                animationTick = 0;
                return;
            }
        }

        if (startAnimation != state) { // If there is a new animation was set.
            resetAnimationTick(); // We're reseting animation, so it can start from the begining.
        }
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    public void setAttacking(boolean isAttacking) {
        this.attacking = isAttacking;
    }


    // for running through massive of sprites with determined speed
    private void updateAnimationTick() {
        animationTick++;  // animationTick is a counter of frames. It gets incremented till specified animationSpeed threshold.
        if (animationTick >= ANI_SPEED) { // Animation index goes 0-1-2-3... till the constant of specified animation and then -0-1-2-3...
            animationTick = 0;
            animationIndex++;
            //
            if (animationIndex >= getSpriteAmount(state)) {
                animationIndex = 0;
                attacking = false; // to attack just once (can be possibly exchanged by setting attacking to false in KeyboardInputs)
                attackChecked = false;
            }
        }

    }

    // Actually it "cuts" specified sprites image into subimages and puts them into animations[][] massive.
    private void loadAnimations() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS); // Getting an animation atlas of player.

        animations = new BufferedImage[7][8]; // depends on player_sprites animation entities/samples

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }


    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirectionBooleans() {
        left = false;
        right = false;
    }

    public void resetAll() {
        resetDirectionBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }


    public int getTileY() {
        return tileY;
    }

    public void powerAttack() {
        if (powerAttackActive)
            return;
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }
    }
}

