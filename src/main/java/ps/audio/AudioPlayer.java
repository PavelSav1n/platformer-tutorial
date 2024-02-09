package ps.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {

    // constants for position of song or effect in audio arrays.
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects; // Clip is a class to play/pause/stop/jump/change volume etc (audioStream input) (.wav only)
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random rand = new Random(); // Random for choosing what attack effect we should play.

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU_1); // When we start the game the first song to play.
    }

    private void loadSongs() {
        String[] songNames = {"menu", "level1", "level2"};
        songs = new Clip[songNames.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(songNames[i]);
        }
    }

    private void loadEffects() {
        String[] effectNames = {"die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3"};
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectNames[i]);
        }
        // If preferences is stored in some config file, we should update volume.
        updateEffectsVolume();
    }

    // Input file returns Clip:
    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio; // Store audio here.

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip(); // We cannot create new Clip (Clip is an interface). We're using AudioSystem.getClip() instead
            c.open(audio);
            return c;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        // To hear changes:
        if (!effectMute)
            playEffect(JUMP);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    // Stopping current song that is playing.
    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    // Plays a particular song depending on level multiplicity (now by 2)
    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0) {
            System.out.println("LEVEL_1.wav");
            playSong(LEVEL_1);
        } else {
            System.out.println("LEVEL_2.wav");
            playSong(LEVEL_2);
        }
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playAttackSound() {
        int start = 4; // Starting index of attacking effect sound.
        start += rand.nextInt(3);
        playEffect(start);
    }

    // Method will play a specified effect sound from effects[] by it effectID.
    public void playEffect(int effectID) {
        // If we'll start to play Clip after it was played already, we will be at the end of a Clip, and it won't play it. So we need to reset it:
        effects[effectID].setMicrosecondPosition(0);
        effects[effectID].start();
    }

    // Method will play a specified song from songs[] by it songID.
    public void playSong(int songID) {
        stopSong();

        currentSongId = songID;
        updateSongVolume(); // Updating song volume to match current preferences.
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY); // Looping mechanics.
    }

    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        //                                        ^ Clip curr playing     we're getting control of this type ^ (there is lots of types of control we can get)
        // gainControl.setValue(0); <-- it's not that simple (so it's more convenient to use external audio libraries)
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum(); // To manipulate volume with variable from 0f to 1f.
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum(); // To manipulate volume with variable from 0f to 1f.
            gainControl.setValue(gain);
        }
    }

}
