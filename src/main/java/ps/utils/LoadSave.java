package ps.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    //    public static final String LEVEL_ONE_DATA = "level_one_data.png";
//    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png"; // bg plate of menu
    public static final String MENU_BACKGROUND_IMG = "background_menu.png"; // bg of menu
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png"; // bg for lvl1
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String MOUNTAIN = "mountain.png";
    public static final String OMON_SPRITE = "omon_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "CANNON_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String CUP = "cup.png";
    public static final String CUP_ATLAS = "cup_sprites.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTIONS_MENU = "options_background.png";
    public static final String WATER_TOP = "water_atlas_animation.png";
    public static final String WATER_BOTTOM = "water.png";
    public static final String GRASS_ATLAS = "grass_atlas.png";
    public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
    public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        // Because of static method we cannot use getClass(), so it is LoadSave.class
        InputStream inputStream = LoadSave.class.getResourceAsStream("/" + fileName);

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

    // Returns imgs array of all lvl sprites in url's folder
    // Each pixel of this lvl files represents an entity of a game (blocks, enemies, objects, etc)
    // Sorting is occurring in Level class.
    public static BufferedImage[] getAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;


        try { // Getting actual resource folder (it's not a "file" in its usual representation in Windows, but in *NIX is, lol)
            file = new File(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        File[] files = file.listFiles(); // Receiving the list of paths to images of lvls.
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) { // sorting files paths
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals(i + 1 + ".png"))
                    filesSorted[i] = files[j];
            }
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        // Filling imgs[] with lvl atlases
        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgs;
    }
}
