package ps.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

    // In IDE filenames are case-insensitive, but in JAR packages they are not!
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String MENU_BUTTONS = "menu.button_atlas.png";
    public static final String MENU_BACKGROUND = "menu.menu_background.png"; // bg plate of menu
    public static final String MENU_BACKGROUND_IMG = "menu.background_menu.png"; // bg of menu
    public static final String PAUSE_BACKGROUND = "menu.pause_menu.png";
    public static final String SOUND_BUTTONS = "menu.sound_button.png";
    public static final String URM_BUTTONS = "menu.urm_buttons.png";
    public static final String VOLUME_BUTTONS = "menu.volume_buttons.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png"; // bg for lvl1
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String MOUNTAIN = "mountain.png";
    public static final String OMON_SPRITE = "omon_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "menu.completed_sprite.png";
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String CUP_ATLAS = "cup_sprites.png";
    public static final String DEATH_SCREEN = "menu.death_screen.png";
    public static final String OPTIONS_MENU = "menu.options_background.png";
    public static final String WATER_TOP = "water_atlas_animation.png";
    public static final String WATER_BOTTOM = "water.png";
    public static final String GRASS_ATLAS = "environment.grass_atlas.png";

    public static final String TREE_ONE_ATLAS = "environment.tree_1_atlas.png";
    public static final String BUSHES_ATLAS = "environment.bushes_atlas.png";
    public static final String FLAG_GEORGIA_ATLAS = "environment.flag_georgia_atlas.png";

    public static final String DIALOGUE_ATTACK_ATLAS = "dialogue.attack.png";
    public static final String DIALOGUE_DEATH_ATLAS = "dialogue.death.png";
    public static final String DIALOGUE_START_ATLAS = "dialogue.start.png";

    public static final String BORDER_POST = "environment.border_post.png";
    public static final String SIGN_LARS = "environment.sign_lars.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        // Because of static method we cannot use getClass(), so it is LoadSave.class
        InputStream inputStream = LoadSave.class.getResourceAsStream("/" + fileName);
        System.out.println(fileName + " | " + inputStream);

        try {
            img = ImageIO.read(inputStream);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return img;
    }

    // Legacy way of loading levels.
    // Logic is to load a folder-file with its contents and then get that list of images and sort them into array of images.
    // Bad news is that JAR is throwing Exception in thread "main" java.lang.RuntimeException: java.lang.IllegalArgumentException: URI is not hierarchical
    // It was solved via getResourceAsStream() method.

//    public static BufferedImage[] getAllLevels() {
//        URL url = LoadSave.class.getResource("/lvls");
//
//        File file = null;
//
//        try {
//            file = new File(url.toURI()); // This file contains information about file URLs in it. Like folder in UNIX. So we can do list() and listFiles() with it.
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        File[] files = file.listFiles(); // Receiving the list of paths to images of lvls.
//        File[] filesSorted = new File[files.length];
//
//        for (int i = 0; i < filesSorted.length; i++) { // sorting files paths
//            for (int j = 0; j < files.length; j++) {
//                if (files[j].getName().equals(i + 1 + ".png"))
//                    filesSorted[i] = files[j];
//            }
//        }
//
//        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
//
//        // Filling imgs[] with lvl atlases
//        for (int i = 0; i < imgs.length; i++) {
//            try {
//                imgs[i] = ImageIO.read(filesSorted[i]);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return imgs;
//    }

    // Returns imgs array of all lvl sprites in url's folder
    // Each pixel of this lvl files represents an entity of a game (blocks, enemies, objects, etc)
    // We must know the exact amount of level files though.
    public static BufferedImage[] getAllLevels() {
        BufferedImage[] imgs = new BufferedImage[3];

        for (int i = 0; i < 3; i++) {
            InputStream is = LoadSave.class.getResourceAsStream("/lvls/" + (i + 1) + ".png");
            System.out.println("is.toString() = " + is.toString());
            try {
                imgs[i] = ImageIO.read(is);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imgs;
    }
}
