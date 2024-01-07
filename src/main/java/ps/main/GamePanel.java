package ps.main;

import ps.inputs.KeyboardInputs;
import ps.inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;


    // constructor
    public GamePanel(Game game) {
        // Controllers:
        mouseInputs = new MouseInputs(this);
        this.game = game;

        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new KeyboardInputs(this)); // passing a class that implements KeyListener
        // other
        setPanelSize();
    }

    // Specify the size of JPanel (GamePanel). Without it, it would be just window with 0x0px grid
    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    // we need this to draw smth in JPanel
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics); // Cleaning the frame to draw a new one. If we don't do that, we may have glitches from previous painting
        game.render(graphics);
    }

    public void updateGame() {

    }

    public Game getGame() {
        return game;
    }
}
