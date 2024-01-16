package ps.ui;

import ps.gamestates.Gamestate;
import ps.gamestates.Playing;
import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ps.utils.Constants.UI.PauseButtons.*;
import static ps.utils.Constants.UI.URMButtons.*;
import static ps.utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private Playing playing; // needed here to pause/unpause game
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;
    private UrmButton menuB, replayB, unpauseB;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
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

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuB.update();
        replayB.update();
        unpauseB.update();

        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        //URM Button
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        //Volume slider
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(mouseEvent, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(mouseEvent, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(mouseEvent, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(mouseEvent, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else if (isIn(mouseEvent, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, musicButton)) {
            if (musicButton.isMousePressed()) // Checking whether pressed mouse is released over pressed button (checking was made in mousePressed() above)
                musicButton.setMuted(!musicButton.isMuted()); // toggle
        } else if (isIn(mouseEvent, sfxButton)) {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted()); // toggle
        } else if (isIn(mouseEvent, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU; // going to menu
                playing.unpauseGame();
            }
        } else if (isIn(mouseEvent, replayB)) {
            if (replayB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }

        } else if (isIn(mouseEvent, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(mouseEvent, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(mouseEvent, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(mouseEvent, menuB))
            menuB.setMouseOver(true);
        else if (isIn(mouseEvent, replayB))
            replayB.setMouseOver(true);
        else if (isIn(mouseEvent, unpauseB))
            unpauseB.setMouseOver(true);
        else if (isIn(mouseEvent, volumeButton))
            volumeButton.setMouseOver(true);
    }

    public void keyPressed(KeyEvent keyEvent) {

    }

    // PauseButton because it's superclass to all buttons in PauseMenu
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
