package entities;

import gamestates.Playing;
import utils.LoadSave;

import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyHandler {
    private Playing playing;
    private BufferedImage[][] goombaImages;
    private ArrayList<Goomba> goombas = new ArrayList<>();
    public EnemyHandler(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        goombas = LoadSave.GetGoombas();
        System.out.println("Goombas: " + goombas.size() + " added.");
    }

    public void update(int[][] levelData) {
        for (Goomba goomba : goombas)
            goomba.update(levelData);
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawGoombas(g, xLevelOffset);
    }

    private void drawGoombas
            (Graphics g, int xLevelOffset) {
        for (Goomba goomba : goombas) {
            g.drawImage(goombaImages[goomba.getEnemyState()][goomba.getAniIndex()], (int)goomba.getHitbox().x-xLevelOffset, (int)goomba.getHitbox().y, GOOMBA_WIDTH, GOOMBA_HEIGHT, null);
        }
    }

    private void loadEnemyImages() {
        goombaImages = new BufferedImage[2][2];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GOOMBA_SPRITE);
        for (int i = 0; i < goombaImages.length; i++)
            for (int j = 0; j < goombaImages[i].length; j++) {
                goombaImages[i][j] = temp.getSubimage(j*GOOMBA_WIDTH_DEFAULT, i*GOOMBA_HEIGHT_DEFAULT, GOOMBA_WIDTH_DEFAULT, GOOMBA_HEIGHT_DEFAULT);
            }
    }
}
