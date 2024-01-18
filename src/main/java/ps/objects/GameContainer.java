package ps.objects;

import ps.main.Game;

import static ps.utils.Constants.ObjectConstants.*;

public class GameContainer extends GameObject {


    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    // We're creating hitboxes here, because there is a difference between boxes and barrels.
    private void createHitBox() {
        if (objType == BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);

        } else {
            initHitbox(23, 25);
            xDrawOffset = (int) (8 * Game.SCALE);
            yDrawOffset = (int) (5 * Game.SCALE);

        }
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

}
