package ui;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasė, atsakinga už žaidimo pabaigos langą.
 */
public class GameOverOverlay {

    private Playing playing;
    private BufferedImage img;
    private int bgX, bgY, bgWidth, bgHeight;

    /**
     * Konstruktorius, kuris sukuria žaidimo pabaigos langą.
     *
     * @param playing žaidimo būsena "Playing"
     */
    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        initImg();
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.GAME_OVER_IMG);
        bgWidth = (int) (img.getWidth() * Game.SCALE);
        bgHeight = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgWidth/2;
        bgY = 0;
    }

    public void update() {

    }

    /**
     * Piešia žaidimo pabaigos langą.
     *
     * @param g grafinis objektas
     */
    public void draw(Graphics g) {
        g.drawImage(img, bgX, bgY, bgWidth, bgHeight, null);
    }
}
