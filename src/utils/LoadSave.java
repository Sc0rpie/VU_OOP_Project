package utils;

import entities.Goomba;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Klasė, atsakinga už paveikslėlių įkėlimą.
 */
public class LoadSave {

    public static final String MARIO_ATLAS = "mario_atlas.png";
    public static final String LEVEL_ATLAS = "1-1_atlas.png";
    public static final String GOOMBA_SPRITE = "goomba_sprite.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String GAME_OVER_IMG = "dead_sprite.png";

    /**
     * Paima sprite atlaso paveikslėlį.
     *
     * @param fileName  Paveikslėlio failo pavadinimas
     * @return sprite atlaso paveikslėlis
     */
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    /**
     * Gauti visus lygius iš resursų aplanko "lvls".
     *
     * @return masyvas su visais lygių paveikslėliais
     */
    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];
        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i+1)+".png"))
                    filesSorted[i] = files[j];
            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

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
