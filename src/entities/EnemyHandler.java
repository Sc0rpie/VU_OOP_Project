package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * EnemyHandler klasė vaizduoja priešų valdymo objektą žaidime.
 * Ji valdo priešų objektų sąrašą ir atnaujina juos.
 */
public class EnemyHandler {

    /**
     * Playing objektas (naudojamas susidūrimo tikrinimui).
     */
    private Playing playing;
    /**
     * Goomba sprites/vaizdai (naudojama animacijoms).
     */
    private BufferedImage[][] goombaImages;
    /**
     * Goomba priešų sąrašas.
     */
    private ArrayList<Goomba> goombas = new ArrayList<>();

    /**
     * Sukuria naują EnemyHandler objektą.
     *
     * @param playing esamasis žaidimo objektas
     */
    public EnemyHandler(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    /**
     * Įkelia priešus iš lygio objekto.
     *
     * @param level lygio objektas su priešais
     */
    public void loadEnemies(Level level) {
        goombas = level.getGoombas();
        System.out.println("Goombos: " + goombas.size());
    }

    /**
     * Atnaujina priešus.
     *
     * @param levelData lygio duomenys
     */
    public void update(int[][] levelData) {
        Iterator<Goomba> iter = goombas.iterator();
        while (iter.hasNext()) {
            Goomba goomba = iter.next();
            if (goomba.getEnemyState() != DEAD)
                goomba.update(levelData, playing);
        }
    }

    /**
     * Nupiešia priešus.
     *
     * @param g             grafinis objektas
     * @param xLevelOffset  lygio horizontalus poslinkis
     */
    public void draw(Graphics g, int xLevelOffset) {
        drawGoombas(g, xLevelOffset);
    }

    /**
     * Nupiešia goomba priešus (tikrinant ar priešas gyvas).
     *
     * @param g             grafinis objektas
     * @param xLevelOffset  lygio horizontalus poslinkis
     */
    private void drawGoombas(Graphics g, int xLevelOffset) {
        for (Goomba goomba : goombas) {
            if (goomba.getEnemyState() != DEAD)
                g.drawImage(goombaImages[goomba.getEnemyState()][goomba.getAniIndex()], (int) goomba.getHitbox().x - xLevelOffset, (int) goomba.getHitbox().y, GOOMBA_WIDTH, GOOMBA_HEIGHT, null);
        }
    }

    /**
     * Įkelia priešo paveikslėlius (naudojama animacijoms).
     */
    private void loadEnemyImages() {
        goombaImages = new BufferedImage[2][2];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GOOMBA_SPRITE);
        for (int i = 0; i < goombaImages.length; i++)
            for (int j = 0; j < goombaImages[i].length; j++) {
                goombaImages[i][j] = temp.getSubimage(j * GOOMBA_WIDTH_DEFAULT, i * GOOMBA_HEIGHT_DEFAULT, GOOMBA_WIDTH_DEFAULT, GOOMBA_HEIGHT_DEFAULT);
            }
    }

    /**
     * Atstatomi visi priešai į pradinę būseną.
     */
    public void resetAllEnemies() {
        for (Goomba g : goombas)
            g.resetEnemy();
    }
}
