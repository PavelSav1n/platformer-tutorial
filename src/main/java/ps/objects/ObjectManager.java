package ps.objects;

import ps.gamestates.Playing;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.Constants.ObjectConstants.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();

        potions = new ArrayList<>();
        containers = new ArrayList<>();
        potions.add(new Potion(300, 300, RED_POTION));
        potions.add(new Potion(400, 300, BLUE_POTION));
        containers.add(new GameContainer(500, 300, BARREL));
        containers.add(new GameContainer(600, 300, BOX));
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int i = 0; i < potionImgs.length; i++) {
            for (int j = 0; j < potionImgs[i].length; j++) {
                potionImgs[i][j] = potionSprite.getSubimage(12 * j, 16 * i, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int i = 0; i < containerImgs.length; i++) {
            for (int j = 0; j < containerImgs[i].length; j++) {
                containerImgs[i][j] = containerSprite.getSubimage(40 * j, 30 * i, 40, 30);
            }
        }
    }

    public void update() {
        for (Potion potion : potions) {
            if (potion.isActive())
                potion.update();
        }
        for (GameContainer container : containers) {
            if (container.isActive())
                container.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer container : containers) {
            if (container.isActive()) {
                int type = 0;
                if (container.getObjType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][container.getAnimationIndex()],
                        (int) (container.getHitbox().x - container.getxDrawOffset() - xLvlOffset),
                        (int) (container.getHitbox().y - container.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion potion : potions) {
            if (potion.isActive()) {
                int type = 0;
                if (potion.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][potion.getAnimationIndex()],
                        (int) (potion.getHitbox().x - potion.getxDrawOffset() - xLvlOffset),
                        (int) (potion.getHitbox().y - potion.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
        }
    }
}



