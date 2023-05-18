package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class LevelCompletedOverlay {

    private Playing playing;
    private BufferedImage img;
    private int bgX, bgY, bgWidth, bgHeight;


    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgWidth = (int) (img.getWidth() * Game.SCALE);
        bgHeight = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgWidth/2;
        bgY = 0;
    }

    public void update() {

    }

    public void draw(Graphics g) {
        g.drawImage(img, bgX, bgY, bgWidth, bgHeight, null);
    }
}
