package ps.main;

import javax.swing.*;

public class GameWindow {
    // we can just extend this class by JFrame
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame(); // it's just for creating GUI window

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when we close GUI window to terminate programm
        jframe.add(gamePanel); // placing a gamePanel in a window
        jframe.setLocationRelativeTo(null); // to spawn GP in the center
        //        jframe.setSize(400, 400); // we're setting the size in gamePanel to avoid borders
        jframe.setResizable(false);
        jframe.pack(); // fit the size of the window to the size of it components
        // visible setter must be at the bottom (to avoid glitches)
        jframe.setVisible(true); // actually to show our frame (default = false)
    }
}
