package ps.gamestates;

import ps.audio.AudioPlayer;
import ps.main.Game;
import ps.ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY()); // if mouse inside rect "e.getX(), e.getY()" we got true. mb.getBounds returns Rectangle
    }

    public Game getGame() {
        return game;
    }

    // Method to change the state of the game.
    public void setGameState(Gamestate state) {
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }
        Gamestate.state = state;
    }
}
