package ps.utils;

import ps.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png"; // each pixel of this file represents an entity of a game (blocks, enemies, objects, etc)
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";

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

    // Returns int 2dArray which is filled with red color int (0-255). It will be mapped on our level.
    public static int[][] GetLevelData() {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        // Going through the image pixel array.
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i)); // getting color of a current pixel
                int value = color.getRed();
                if (value >= 48)
                    value = 0; // If there's a mistake in initial image, we can easily overcome the 48 red int value of our sprite array. So here is protection.
                lvlData[i][j] = value; // saving red data to 2dArray
            }
        }
        return lvlData;
    }
}
