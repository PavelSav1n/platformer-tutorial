package ps.main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    // we can just extend this class with JFrame
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame(); // it's just for creating GUI window

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when we close GUI window to terminate programm
        jframe.add(gamePanel); // placing a gamePanel in a window
        jframe.setLocation(0, 0); // to spawn GP at 0 x 0 px.
        //        jframe.setSize(400, 400); // we're setting the size in gamePanel to avoid borders
        jframe.setResizable(false);
        jframe.pack(); // fit the size of the window to the size of it components
        // visible setter must be at the bottom (to avoid glitches)
        jframe.setVisible(true); // actually to show our frame (default = false)
        jframe.addWindowFocusListener(new WindowFocusListener() { // In case we loose focus on our game window
            @Override
            public void windowGainedFocus(WindowEvent e) {
                System.out.println("FocusBack!");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost(); // Make all boolean false (stop running, etc.)
                System.out.println("LostFocus!");
            }
        });
    }
}
