package ps.ui;

import ps.gamestates.Gamestate;
import ps.gamestates.Playing;
import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ps.utils.Constants.UI.URMButtons.URM_SIZE;

public class PauseOverlay {

    private Playing playing; // needed here to pause/unpause game
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private AudioOptions audioOptions;
    private UrmButton menuB, replayB, unpauseB;


    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();

    }


    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);

    }


    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();

        audioOptions.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);


        //URM Button
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        audioOptions.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(mouseEvent, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(mouseEvent, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else
            audioOptions.mousePressed(mouseEvent);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, menuB)) {
            if (menuB.isMousePressed()) {
                playing.resetAll(); // To restart lvl when we go to the menu.
                playing.setGameState(Gamestate.MENU); // going to menu and playing menu song.
                playing.unpauseGame();
            }
        } else if (isIn(mouseEvent, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }

        } else if (isIn(mouseEvent, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        } else
            audioOptions.mouseReleased(mouseEvent);

        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);

        if (isIn(mouseEvent, menuB))
            menuB.setMouseOver(true);
        else if (isIn(mouseEvent, replayB))
            replayB.setMouseOver(true);
        else if (isIn(mouseEvent, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(mouseEvent);
    }

    public void keyPressed(KeyEvent keyEvent) {

    }

    // PauseButton because it's superclass to all buttons in PauseMenu
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
