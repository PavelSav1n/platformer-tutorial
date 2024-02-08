package ps.ui;

import ps.gamestates.Gamestate;
import ps.main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static ps.utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static ps.utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static ps.utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {

    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;
    private Game game;

    public AudioOptions(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        volumeButton.update();

    }

    public void draw(Graphics g) {

        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        //Volume slider
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeX(e.getX()); // Changing X changes visual position and floatValue for audio level.
            float valueAfter = volumeButton.getFloatValue();
            if (valueBefore != valueAfter)
                game.getAudioPlayer().setVolume(valueAfter); // Here we're using floatValue to actually change audio level.
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(mouseEvent, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(mouseEvent, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (isIn(mouseEvent, musicButton)) {
            if (musicButton.isMousePressed()) { // Checking whether pressed mouse is released over pressed button (checking was made in mousePressed() above)
                musicButton.setMuted(!musicButton.isMuted()); // Changing appearance.
                game.getAudioPlayer().toggleSongMute(); // Actual changing music mute.
            }
        } else if (isIn(mouseEvent, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted()); // Changing appearance.
                game.getAudioPlayer().toggleEffectMute(); // Actual changing effect mute.
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();

        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        volumeButton.setMouseOver(false);

        if (isIn(mouseEvent, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(mouseEvent, sfxButton))
            sfxButton.setMouseOver(true);

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
