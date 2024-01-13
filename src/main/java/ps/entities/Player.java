package ps.entities;

import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ps.utils.Constants.PlayerConstants.*;
import static ps.utils.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 20; // for running through massive of sprites with determined speed
    private int playerAction = IDLE; // Animation current state. Used for accessing to specified row of animations[][] massive.
    private int playerDirection = -1; // Moving state. If not moving =-1, otherwise it's 0, 1, 2 or 3
    private boolean moving = false;
    private boolean attacking = false;
    private boolean up, left, right, down, jump;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData; // We are storing lvl data in player class just for now. We need it to detect collision.
    private float xDrawOffset = 21 * Game.SCALE; // Offset where the new hitbox starts (not 0x0 but 21x4). A player drawing will use this.
    private float yDrawOffset = 4 * Game.SCALE; // Sprite of the player is a bit more than hitbox, so we need some offsets to center sprite
    // Jumping / Gravity
    private float airSpeed = 0f; // Speed at what player travelling through the air (jumping or falling)
    private float gravity = 0.04f * Game.SCALE; // How fast player will fall back down.
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE)); // Initializing & Drawing hitbox with a size of 20x28 at x, y.
    }

    public void update() {
        updatePosition(); // if moving updating position
//        updateHitbox(); //
        updateAnimationTick();
        setAnimation(); // to set proper playerAction
    }

    public void render(Graphics graphics, int lvlOffset) {
        // getting image and drawing width x height (64x40 -- default) part at animations[i][j] of initial image
        // hitbox.x -xDrawOffset is where to draw a player.
        // We're drawing a player after drawing a hitbox, with a bit of offset.
        graphics.drawImage(animations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(graphics, lvlOffset);
    }

    // Updating position so, that we can hold two keys and run diagonally or stop moving at all.
    // Also checking, whether it's possible to go in chosen direction.
    // Moving now depends on hitbox of a player (20x28), not a player original sprite size (64x40).
    private void updatePosition() {
        moving = false; // false by default

        if (jump) {
            jump();
        }
        // If A+D or not A+D we should not be here.
        if (!inAir)
            if ((!left && !right) || (right && left)) return;

        float xSpeed = 0; // Speed is by default 0.

        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;
        if (!inAir) {
            if (!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }


        if (inAir) {
            if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
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
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }
    }


    private void setAnimation() {
        int startAnimation = playerAction; // Remember what current animation is before checking is there a switch.

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMPING;
            else
                playerAction = FALLING;
        }

        if (attacking) { // it is here
            playerAction = ATTACK_1;
        }

        if (startAnimation != playerAction) { // If there is a new animation was set.
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
        if (animationTick >= animationSpeed) { // Animation index goes 0-1-2-3... till the constant of specified animation and then -0-1-2-3...
            animationTick = 0;
            animationIndex++;
            //
            if (animationIndex >= getSpriteAmount(playerAction)) {
                animationIndex = 0;
                attacking = false; // to attack just once (can be possibly exchanged by setting attacking to false in KeyboardInputs)
            }
        }

    }

    // Actually it "cuts" specified sprites image into subimages and puts them into animations[][] massive.
    private void loadAnimations() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS); // Getting an animation atlas of player.

        animations = new BufferedImage[9][6]; // depends on player_sprites animation entities/samples

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
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

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirectionBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}

