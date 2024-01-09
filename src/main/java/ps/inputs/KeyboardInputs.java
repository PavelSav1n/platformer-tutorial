package ps.inputs;

import ps.gamestates.Gamestate;
import ps.main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static ps.utils.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    // constructor
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
        }
    }

    // When key released stop moving (animation and transition)
    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> {
                gamePanel.getGame().getPlaying().keyReleased(e);
            }

            case MENU -> {
                gamePanel.getGame().getMenu().keyReleased(e);
            }
        }
    }
}
