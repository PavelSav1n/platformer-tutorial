package ps.ui;

import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ps.utils.Constants.UI.PauseButtons.*;

public class PauseOverlay {

    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;

    public PauseOverlay() {
        loadBackground();
        createSoundButtons();
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
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);
    }

    public void mouseDragged() {

    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(mouseEvent, sfxButton))
            sfxButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, musicButton)) {
            if (musicButton.isMousePressed()) // Checking whether pressed mouse is released over pressed button (checking was made in mousePressed() above)
                musicButton.setMuted(!musicButton.isMuted()); // toggle
        } else if (isIn(mouseEvent, sfxButton))
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted()); // toggle

        musicButton.resetBools();
        sfxButton.resetBools();
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        if (isIn(mouseEvent, musicButton))
            musicButton.setMouseOver(true);
        if (isIn(mouseEvent, sfxButton))
            sfxButton.setMouseOver(true);
    }

    public void keyPressed(KeyEvent keyEvent) {

    }

    // PauseButton because it's superclass to all buttons in PauseMenu
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
