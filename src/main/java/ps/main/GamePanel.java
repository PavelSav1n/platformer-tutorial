package ps.main;

import ps.inputs.KeyboardInputs;
import ps.inputs.MouseInputs;
import ps.utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static ps.utils.Constants.PlayerConstants.*;
import static ps.utils.Constants.Directions.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    private int frames = 0; // frames that will be counted
    private BufferedImage img;
    private BufferedImage[][] animations; // array for all animation sprites such as idle, death, running and so on.
    private int animationTick, animationIndex, animationSpeed = 20; // for running through massive of sprites with determined speed
    private int playerAction = IDLE;
    private int playerDirection = -1; // if not moving =-1, otherwise it's 0, 1, 2 or 3
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

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

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

        // getting image and drawing 64x40 part at idleAnimation[i] of initial image
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

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }

    }

}
