package ps.entities;

import ps.gamestates.Playing;
import ps.levels.Level;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.Constants.EnemyConstants.*;

// Like LevelManager this class will take care of enemy patrol, attack and other functions
public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] omonArr;
    private ArrayList<Omon> omons = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;

        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        omons = level.getOmons(); // getting OMON list from level class.
    }

    // Just a way to update each element in ArrayList<Omon> (real OMON objs)
    public void update(int[][] lvlData) {
        boolean isAnyActive = false;
        for (Omon omon : omons) {
            if (omon.isActive()) {// If DEAD, not going to update
                omon.update(lvlData, playing);
                isAnyActive = true;
            }
        }
        // If we want to GAME OVER when there are no more enemies.
        if (!isAnyActive)
//            playing.setLevelCompleted(true);
            System.out.println("All enemies down.");
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawOmon(g, xLvlOffset);
    }

    // To draw real OMON objs we need to know what state and what aniIndex we're drawing.
    // X if offseted by 1 -- lvl movement, 2 -- hitbox offset to fit sprites in hitbox, 3 -- flipX when they are walking in LEFT direction
    // width is multiplied by -1 when OMON go LEFT.
    private void drawOmon(Graphics g, int xLvlOffset) {
        for (Omon omon : omons) {
            if (omon.isActive()) {
                if (omon.state == DEAD) { // This check is needed because I don't need death animation to flipX
                    g.drawImage(
                            omonArr[omon.getState()][omon.getAniIndex()],
                            (int) omon.getHitbox().x - xLvlOffset - OMON_DRAWOFFSET_X,
                            (int) omon.getHitbox().y - OMON_DRAWOFFSET_Y + (int) omon.getPushDrawOffset(),
                            OMON_WIDTH, OMON_HEIGHT, null);

                    omon.drawHitbox(g, xLvlOffset);
                    omon.drawAttackBox(g, xLvlOffset);
                } else
                    g.drawImage( // This is universal approach
                            omonArr[omon.getState()][omon.getAniIndex()],
                            (int) omon.getHitbox().x - xLvlOffset - OMON_DRAWOFFSET_X + omon.flipX(),
                            (int) omon.getHitbox().y - OMON_DRAWOFFSET_Y + (int) omon.getPushDrawOffset(),
                            OMON_WIDTH * omon.flipW(), OMON_HEIGHT, null);
                omon.drawHitbox(g, xLvlOffset);
                omon.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    // Player damage to Enemies with a stick:
    public int checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Omon omon : omons) {
            if (omon.getCurrentHealth() > 0) // Without this check dead enemy stays in death animation while we're hitting him.
                if (omon.isActive()) {
                    if (attackBox.intersects(omon.getHitbox())) {
                        omon.hurt(10);
                        return 1;
                    }
                }
        }
        return 0;
    }

    // Player damage with a plastic cup:
    public int checkEnemyHitWithCup(Rectangle2D.Float attackBox) {
        for (Omon omon : omons) {
            if (omon.getCurrentHealth() > 0) // Without this check dead enemy stays in death animation while we're hitting him.
                if (omon.isActive()) {
                    if (attackBox.intersects(omon.getHitbox())) {
                        omon.hurt(100);
                        return 1;
                    }
                }
        }
        return 0;
    }

    private void loadEnemyImgs() {
        omonArr = new BufferedImage[7][8];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.OMON_SPRITE);

        for (int i = 0; i < omonArr.length; i++) {
            for (int j = 0; j < omonArr[i].length; j++) {
                omonArr[i][j] = temp.getSubimage(j * OMON_WIDTH_DEFAULT, i * OMON_HEIGHT_DEFAULT, OMON_WIDTH_DEFAULT, OMON_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies() {
        for (Omon omon : omons) {
            omon.resetEnemy();
        }
    }
}
