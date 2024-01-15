package ps.entities;

import ps.gamestates.Playing;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.Constants.UI.EnemyConstants.*;

// Like LevelManager this class will take care of enemy patrol, attack and other functions
public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;

        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.getCrabs(); // getting crabbies list from level data.
        System.out.println("size of crabs: " + crabbies.size());
    }

    public void update(int[][] lvlData, Player player) {
        for (Crabby crabby : crabbies) {
            if (crabby.isActive()) // If DEAD, not going to update
                crabby.update(lvlData, player);
        }// Just a peculiar way to update each element in ArrayList<Crabby> (real crab objs)
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    // To draw real crab objs we need to know what state and what aniIndex we're drawing.
    // X if offseted by 1 -- lvl movement, 2 -- hitbox offset to fit sprites in hitbox, 3 -- flipX when they are walking in RIGHT direction
    // width is multiplied by -1 when crabbies go RIGHT.
    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby crabby : crabbies) {
            if (crabby.isActive()) {
                g.drawImage(
                        crabbyArr[crabby.getEnemyState()][crabby.getAniIndex()],
                        (int) crabby.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + crabby.flipX(),
                        (int) crabby.getHitbox().y - CRABBY_DRAWOFFSET_Y,
                        CRABBY_WIDTH * crabby.flipW(), CRABBY_HEIGHT, null);
                crabby.drawHitbox(g, xLvlOffset);
                crabby.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby crabby : crabbies) {
            if (crabby.isActive()) {
                if (attackBox.intersects(crabby.getHitbox())) {
                    crabby.hurt(10);
                    return;
                }
            }
        }

    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);

        for (int i = 0; i < crabbyArr.length; i++) {
            for (int j = 0; j < crabbyArr[i].length; j++) {
                crabbyArr[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);

            }

        }
    }

    public void resetAllEnemies() {
        for (Crabby crabby : crabbies) {
            crabby.resetEnemy();
        }
    }
}
