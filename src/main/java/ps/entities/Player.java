package ps.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static ps.utils.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 20; // for running through massive of sprites with determined speed
    private int playerAction = IDLE; // Animation current state. Used for accessing to specified row of animations[][] massive.
    private int playerDirection = -1; // Moving state. If not moving =-1, otherwise it's 0, 1, 2 or 3
    private boolean moving = false;
    private boolean attacking = false;
    private boolean up, left, right, down;
    private float playerSpeed = 2.0f;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        updatePosition(); // if moving updating position
        updateAnimationTick();
        setAnimation(); // to set proper playerAction
    }

    public void render(Graphics graphics) {
        // getting image and drawing 64x40 part at animations[i][j] of initial image
        graphics.drawImage(animations[playerAction][animationIndex], (int) x, (int) y, 256, 160, null);

    }

    // Updating position so, that we can hold two keys and run diagonally or stop moving at all.
    private void updatePosition() {
        moving = false; // false by default

        if (left && !right) {
            x -= playerSpeed;
            moving = true; // if moving then true
        } else if (right && !left) {
            x += playerSpeed;
            moving = true;
        }

        if (up && !down) {
            y -= playerSpeed;
            moving = true;
        } else if (down && !up) {
            y += playerSpeed;
            moving = true;
        }
    }

    private void setAnimation() {
        int startAnimation = playerAction; // Remember what current animation is before checking is there a switch.

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
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

        InputStream inputStream = getClass().getResourceAsStream("/player_sprites.png");

        try {
            BufferedImage img = ImageIO.read(inputStream);


            animations = new BufferedImage[9][6]; // depends on player_sprites animation entities/samples

            for (int j = 0; j < animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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

    public void resetDirectionBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}

