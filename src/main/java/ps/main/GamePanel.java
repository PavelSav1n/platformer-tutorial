package ps.main;

import ps.inputs.KeyboardInputs;
import ps.inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static ps.utils.Constants.Directions.*;
import static ps.utils.Constants.PlayerConstants.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta = 0, yDelta = 0; // Coordinates where image should be drawn.
//    private int frames = 0; // frames that will be counted
    private BufferedImage img; // Initial image, where all sprites are.
    private BufferedImage[][] animations; // Array for all separate animation sprites such as idle, death, running and so on.
    private int animationTick, animationIndex, animationSpeed = 20; // for running through massive of sprites with determined speed
    private int playerAction = IDLE; // Animation current state. Used for accessing to specified row of animations[][] massive.
    private int playerDirection = -1; // Moving state. If not moving =-1, otherwise it's 0, 1, 2 or 3
    private boolean moving = false;


    // constructor
    public GamePanel() {
        // Controllers:
        mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new KeyboardInputs(this)); // passing a class that implements KeyListener
        // other
        setPanelSize();
        importImg(); // reading img from file
        loadAnimations();
    }

    // Actually it "cuts" specified sprites image into subimages and puts them into animations[][] massive.
    private void loadAnimations() {
        animations = new BufferedImage[9][6]; // depends on player_sprites animation entities/samples

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    private void importImg() {
        InputStream inputStream = getClass().getResourceAsStream("/player_sprites.png");

        try {
            img = ImageIO.read(inputStream);
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

    // Specify the size of JPanel (GamePanel). Without it, it would be just window with 0x0px grid
    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    // Setter for playerDirection
    public void setDirection(int direction) {
        this.playerDirection = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    // we need this to draw smth in JPanel
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics); // Cleaning the frame to draw a new one. If we don't do that, we may have glitches from previous painting

        updateAnimationTick();

        setAnimation();
        updatePosition();

        // getting image and drawing 64x40 part at animations[i][j] of initial image
        graphics.drawImage(animations[playerAction][animationIndex], (int) xDelta, (int) yDelta, 256, 160, null);
    }

    private void updatePosition() {
        if (moving) {
            switch (playerDirection) {
                case LEFT:
                    xDelta -= 5;
                    break;
                case UP:
                    yDelta -= 5;
                    break;
                case RIGHT:
                    xDelta += 5;
                    break;
                case DOWN:
                    yDelta += 5;
                    break;
            }
        }
    }

    private void setAnimation() {
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
    }

    // for running through massive of sprites with determined speed
    // AnimationTick is a counter of frames. It gets incremented till specified animationSpeed threshold.
    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) { // Animation index goes 0-1-2-3... till the constant of specified animation and then -0-1-2-3...
            animationTick = 0;
            animationIndex++;
            //
            if (animationIndex >= getSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }

    }

}
