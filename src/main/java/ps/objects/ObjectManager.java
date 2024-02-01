package ps.objects;

import ps.entities.Player;
import ps.gamestates.Playing;
import ps.levels.Level;
import ps.main.Game;
import ps.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ps.utils.Constants.ObjectConstants.*;
import static ps.utils.Constants.Projectiles.*;
import static ps.utils.HelpMethods.canCannonSeePlayer;
import static ps.utils.HelpMethods.IsProjectileHittingLevel;

// ObjectManager class provides mechanics such as follows:
// - Creating a copies of potion & container objs, and initial objects of spikes and cannons
// - Creating subimages from objs ATLAS and filling objects arrays with according samples.
// - Creating a projectiles
// - Intersecting a Player hitbox with spikes hitbox, potion hitbox.
// - Intersecting a player attackBox with container hitbox.
// - Applying potion effects to a Player.
// - Drawing objects (potions, containers, spikes, cannons).
// - Updating active objects.
// - Reseting all objects to default state.
public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImg;
    private BufferedImage spikeImg, cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>(); // each time a cannon shoots, we'll be adding balls to this array list.

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Player p) {
        for (Spike spike : spikes) {
            if (spike.getHitbox().intersects(p.getHitbox()))
                p.kill();
        }
    }

    // Whether player hitbox interact with container hitbox.
    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion potion : potions) {
            if (potion.isActive()) {
                if (hitbox.intersects(potion.getHitbox())) {
                    potion.setActive(false);
                    applyEffectToPlayer(potion);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjType() == RED_POTION) {
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    // Whether player attackBox interacts with container hitbox.
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer container : containers) {
            if (container.isActive() && !container.doAnimation) {
                if (container.getHitbox().intersects(attackbox)) {
                    container.setDoAnimation(true);
                    int type = 0;
                    if (container.getObjType() == BARREL) // spawning potion at the place of container
                        type = 1;
                    potions.add(new Potion(
                            (int) (container.getHitbox().x + container.getHitbox().width / 2),
                            (int) (container.getHitbox().y - container.getHitbox().height / 2.5), type));
                    return; // to not destroy 2 boxes at the same time
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
//        potions = newLevel.getPotions();
//        containers = newLevel.getContainers();

        potions = new ArrayList<>(newLevel.getPotions()); // Creating a copies of initial ArrayList of level objects.
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        projectiles.clear(); // Clearing array list, when we start level, we don't need balls from previous game.

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

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS); // Just an image, not an array.

        cannonImg = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImg.length; i++) {
            cannonImg[i] = temp.getSubimage(i * CANNON_WIDTH_DEFAULT, 0, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);
        }

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
    }

    public void update(int[][] lvlData, Player player) {
        for (Potion potion : potions) {
            if (potion.isActive())
                potion.update();
        }
        for (GameContainer container : containers) {
            if (container.isActive())
                container.update();
        }

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                projectile.updatePos();
                if(projectile.getHitbox().intersects(player.getHitbox())){
                    player.changeHealth(-25);
                    projectile.setActive(false);
                } else if (IsProjectileHittingLevel(projectile, lvlData)) {
                    projectile.setActive(false);
                }
            }
        }
    }



    /* if the cannon is not animating
     * tileY is same
     * ifPlayer is in range
     * is player in front of cannon
     * lin of sight
     * shoot the cannon
     */
    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon cannon : cannons) {
            if (!cannon.doAnimation)
                if (cannon.getTileY() == player.getTileY()) {
                    if (isPlayerInRange(cannon, player)) {
                        if (isPlayerInfrontOfCannon(cannon, player)) {
                            if (canCannonSeePlayer(lvlData, player.getHitbox(), cannon.getHitbox(), cannon.getTileY())) {
                                cannon.setDoAnimation(true); // we must shoot only on particular animation, not at the start.
                            }
                        }
                    }
                }
            cannon.update();
            if (cannon.getAnimationIndex() == 4 && cannon.getAnimationTick() == 0) // we shoot only if animation is in 4th position and only if cannon is not shooting currently.
                shootCannon(cannon);
        }
//        }
    }

    private void shootCannon(Cannon cannon) {
        cannon.setDoAnimation(true);
        int dir = 1;
        if (cannon.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int) cannon.getHitbox().x, (int) cannon.getHitbox().y, dir));
    }

    private boolean isPlayerInfrontOfCannon(Cannon cannon, Player player) {
        if (cannon.getObjType() == CANNON_LEFT) {
            if (player.getHitbox().x < cannon.getHitbox().x)
                return true;
        } else if (player.getHitbox().x > cannon.getHitbox().x) {
            return true;
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon cannon, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - cannon.getHitbox().x); // Evaluating distance between player and enemy (module of distance is absolute (abs))
        return absValue <= Game.TILES_SIZE * 5; // Eye of sight is 5 times larger than TILES_SIZE
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile projectile : projectiles) {
            if (projectile.isActive())
                g.drawImage(cannonBallImg, (int) (projectile.getHitbox().x - xLvlOffset), (int) projectile.getHitbox().y, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon cannon : cannons) {
            int x = (int) (cannon.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;
            if (cannon.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImg[cannon.getAnimationIndex()], x, (int) cannon.getHitbox().y, width, CANNON_HEIGHT, null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike spike : spikes) {
            g.drawImage(spikeImg, (int) (spike.getHitbox().x - xLvlOffset), (int) (spike.getHitbox().y - spike.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
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

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel()); // Creating new copies of initial obj arrays.

        // Reseting all initial containers and potions.
        for (Potion potion : potions) {
            potion.reset();
        }
        for (GameContainer container : containers) {
            container.reset();
        }

        for (Cannon cannon : cannons) {
            cannon.reset();
        }
    }


}



