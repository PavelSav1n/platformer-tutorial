package ps.inputs;

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(true);
//                System.out.println("w");
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(true);
//                System.out.println("a");
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(true);
//                System.out.println("s");
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(true);
//                System.out.println("d");
                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame().getPlayer().setJump(true);
                break;
        }

    }

    // When key released stop moving (animation and transition)
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(false);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(false);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(false);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame().getPlayer().setJump(false);
                break;
        }
    }
}
