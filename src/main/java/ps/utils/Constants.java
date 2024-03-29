package ps.utils;

import ps.main.Game;

public class Constants {

    public static final float GRAVITY = 0.04f * Game.SCALE; // How fast entity will fall when inAir.
    public static final int ANI_SPEED = 25;
    public static final int TEXTURE_MAX_AMOUNT = 62;

    public static class Dialogue {
        public static final int DIALOGUE_ATTACK = 0;
        public static final int DIALOGUE_DEATH = 1;
        public static final int DIALOGUE_START = 2;

        public static final int DIALOGUE_ATTACK_WIDTH_DEFAULT = 35;
        public static final int DIALOGUE_ATTACK_HEIGHT_DEFAULT = 17;
        public static final int DIALOGUE_ATTACK_WIDTH = (int) (DIALOGUE_ATTACK_WIDTH_DEFAULT * Game.SCALE);
        public static final int DIALOGUE_ATTACK_HEIGHT = (int) (DIALOGUE_ATTACK_HEIGHT_DEFAULT * Game.SCALE);

        public static final int DIALOGUE_DEATH_WIDTH_DEFAULT = 39;
        public static final int DIALOGUE_DEATH_HEIGHT_DEFAULT = 17;
        public static final int DIALOGUE_DEATH_WIDTH = (int) (DIALOGUE_DEATH_WIDTH_DEFAULT * Game.SCALE);
        public static final int DIALOGUE_DEATH_HEIGHT = (int) (DIALOGUE_DEATH_HEIGHT_DEFAULT * Game.SCALE);

        public static final int DIALOGUE_START_WIDTH_DEFAULT = 73;
        public static final int DIALOGUE_START_HEIGHT_DEFAULT = 40;
        public static final int DIALOGUE_START_WIDTH = (int) (DIALOGUE_START_WIDTH_DEFAULT * Game.SCALE);
        public static final int DIALOGUE_START_HEIGHT = (int) (DIALOGUE_START_HEIGHT_DEFAULT * Game.SCALE);

        public static int GetSpriteAmount(int type) {
            return switch (type) {
                case DIALOGUE_ATTACK, DIALOGUE_DEATH -> 10;
                case DIALOGUE_START -> 20;
                default -> 0;
            };

        }
    }

    public static class Projectiles {
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
        public static final int CANNON_BALL_WIDTH = (int) (CANNON_BALL_DEFAULT_WIDTH * Game.SCALE);
        public static final int CANNON_BALL_HEIGHT = (int) (CANNON_BALL_DEFAULT_HEIGHT * Game.SCALE);

        public static final int CUP_DEFAULT_WIDTH = 11;
        public static final int CUP_DEFAULT_HEIGHT = 10;
        public static final int CUP_WIDTH = (int) (CUP_DEFAULT_WIDTH * Game.SCALE);
        public static final int CUP_HEIGHT = (int) (CUP_DEFAULT_HEIGHT * Game.SCALE);

        public static final float SPEED = 0.75f * Game.SCALE;
    }

    public static class ObjectConstants {
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int TREE = 7;
        public static final int BUSH = 8;
        public static final int FLAG_GEORGIA = 9;
        public static final int CUP = 10;
        public static final int CANNON_BALL = 11;
        public static final int BORDER_POST = 12;
        public static final int SIGN_LARS = 13;

        public static final int SIGN_LARS_WIDTH_DEFAULT = 25; // 23 straight
        public static final int SIGN_LARS_HEIGHT_DEFAULT = 28; // 27 straight
        public static final int SIGN_LARS_WIDTH = (int) (SIGN_LARS_WIDTH_DEFAULT * Game.SCALE);
        public static final int SIGN_LARS_HEIGHT = (int) (SIGN_LARS_HEIGHT_DEFAULT * Game.SCALE);

        public static final int BORDER_POST_WIDTH_DEFAULT = 726;
        public static final int BORDER_POST_HEIGHT_DEFAULT = 130;
        public static final int BORDER_POST_WIDTH = (int) (BORDER_POST_WIDTH_DEFAULT * Game.SCALE);
        public static final int BORDER_POST_HEIGHT = (int) (BORDER_POST_HEIGHT_DEFAULT * Game.SCALE);


        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * Game.SCALE);
        public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * Game.SCALE);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * Game.SCALE);
        public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * Game.SCALE);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * Game.SCALE);
        public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * Game.SCALE);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * Game.SCALE);
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * Game.SCALE);

        public static int getSpriteAmount(int objType) {
            switch (objType) {
                case TREE -> {
                    return 6;
                }
                case RED_POTION, BLUE_POTION, CANNON_LEFT, CANNON_RIGHT, FLAG_GEORGIA -> {
                    return 7;
                }
                case BARREL, BOX, CUP -> {
                    return 8;
                }
            }
            return 1;
        }

        public static int GetTreeOffsetX(int treeType) {
            return switch (treeType) {
                case TREE, BUSH, FLAG_GEORGIA -> (Game.TILES_SIZE / 2) - (GetEnvObjWidth(treeType) / 2);
//                case TREE_TWO -> (int) (Game.TILES_SIZE / 2.5f);
//                case TREE_THREE -> (int) (Game.TILES_SIZE / 1.65f);
                default -> 0;
            };

        }

        public static int GetTreeOffsetY(int treeType) {

            return switch (treeType) {
                case TREE, BUSH, FLAG_GEORGIA -> -GetEnvObjHeight(treeType) + Game.TILES_SIZE;
//                case TREE_THREE -> -GetTreeHeight(treeType) + (int) (Game.TILES_SIZE / 1.25f);
                default -> 0;
            };

        }

        public static int GetEnvObjDefaultWidth(int envObjType) {
            return switch (envObjType) {
                case TREE, BUSH -> 58;
                case FLAG_GEORGIA -> 28;
                default -> 0;
            };
        }

        public static int GetEnvObjDefaultHeight(int envObjType) {
            return switch (envObjType) {
                case TREE, FLAG_GEORGIA -> 73;
                case BUSH -> 40;
                default -> 0;
            };
        }


        // If we want to flip obj, just add "-" in return.
        public static int GetEnvObjWidth(int envObjType) {
            return switch (envObjType) {
                case TREE, BUSH -> (int) (58 * Game.SCALE);
//                case TREE_TWO -> (int) (62 * Game.SCALE);
                case FLAG_GEORGIA -> (int) (GetEnvObjDefaultWidth(FLAG_GEORGIA) * Game.SCALE);
                default -> 0;
            };
        }

        public static int GetEnvObjHeight(int treeType) {
            return switch (treeType) {
                case TREE, FLAG_GEORGIA -> (int) (73 * Game.SCALE);
                case BUSH -> (int) (GetEnvObjDefaultHeight(BUSH) * Game.SCALE);
                default -> 0;
            };
        }
    }

    public static class EnemyConstants {
        public static final int OMON = 0;
        public static final int OMON_MAX_HEALTH = 100;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;

        public static final int OMON_WIDTH_DEFAULT = 64;
        public static final int OMON_HEIGHT_DEFAULT = 40;

        public static final int OMON_WIDTH = (int) (OMON_WIDTH_DEFAULT * Game.SCALE);
        public static final int OMON_HEIGHT = (int) (OMON_HEIGHT_DEFAULT * Game.SCALE);

        public static final int OMON_DRAWOFFSET_X = (int) (22 * Game.SCALE); // Margin from X border of sprite to x border of hitbox
        public static final int OMON_DRAWOFFSET_Y = (int) (4 * Game.SCALE); // same here for Y

        public static int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case OMON:
                    switch (enemyState) {
                        case IDLE:
//                            return 9;
                            return 5;
                        case RUNNING:
                            return 6;
                        case ATTACK:
//                            return 7;
                            return 3;
                        case HIT:
                            return 4;
                        case DEAD:
//                            return 5;
                            return 8;
                    }
            }
            return 0;
        }

        public static int getMaxHealth(int enemyType) {
            switch (enemyType) {
                case OMON -> {
                    return OMON_MAX_HEALTH;
                }
                default -> {
                    return 1;
                }
            }
        }

        public static int getEnemyDmg(int enemyType) {
            switch (enemyType) {
                case OMON -> {
                    return 15;
                }
                default -> {
                    return 0;
                }
            }
        }


    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int MOUNTAIN_WIDTH_DEFAULT = 1725;
        public static final int MOUNTAIN_HEIGHT_DEFAULT = 752;

        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int MOUNTAIN_WIDTH = (int) (MOUNTAIN_WIDTH_DEFAULT / 3 * Game.SCALE);
        public static final int MOUNTAIN_HEIGHT = (int) (MOUNTAIN_HEIGHT_DEFAULT / 3 * Game.SCALE);
    }

    public static class UI {
        public static class Buttons {
            // Width & height of UI Menu with scaling:
            public static final int B_WIDTH_DEFAULT = 140; // default values is for cropping original image
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE); // this is for ingame after scaling.
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42; // width & height is identical
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);

        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56; // width & height is identical
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }

    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMPING = 2;
        public static final int FALLING = 3;
        public static final int HIT = 5;
        public static final int ATTACK = 4;
        public static final int DEAD = 6;
        public static final int THROWING = 7;

        public static int getSpriteAmount(int player_action) {

            switch (player_action) {
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMPING:
                case ATTACK, THROWING:
                    return 3;
                case DEAD:
                    return 8;
                case FALLING:
                default:
                    return 1;
            }
        }
    }
}
