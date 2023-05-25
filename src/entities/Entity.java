package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Abstrakti bazinė klasė, kuri aprašo "Entity" žaidime.
 */
public abstract class Entity {

    /**
     * x ir y koordinatės.
     */
    protected float x,y;
    /**
     * "Entity" plotis ir aukštis.
     */
    protected int width, height;
    /**
     * "Entity" hitbox (naudojamas susidūrimams).
     */
    protected Rectangle2D.Float hitbox;
    /**
     * Animacijos kintamieji (aniTick - kiek laiko praeina tarp kiekvieno animacijos kadrų, aniIndex - animacijos kadro indeksas).
     */
    protected int aniTick, aniIndex;
    /**
     * "Entity" būsena (state).
     */
    protected int state;
    /**
     * "Entity" greitis ore.
     */
    protected float airSpeed = 0f;
    /**
     * Ar "Entity" ore.
     */
    protected boolean inAir = false;

    /**
     * Sukuria naują entiteto objektą su nurodytomis koordinatėmis, plotį ir aukštį.
     *
     * @param x      entiteto horizontalė koordinatė
     * @param y      entiteto vertikalė koordinatė
     * @param width  entiteto plotis
     * @param height entiteto aukštis
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Nupiešia entiteto hitbox (atvaizduojant debug režime).
     *
     * @param g            grafinis objektas
     * @param xLevelOffset lygio horizontalus poslinkis
     */
    protected void drawHitbox(Graphics g, int xLevelOffset) {
        //Hitbox debug
        g.setColor(Color.RED);
        g.drawRect((int)hitbox.x - xLevelOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    /**
     * Inicializuoja entiteto hitbox.
     *
     * @param width  hitbox plotis
     * @param height hitbox aukštis
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    /**
     * Grąžina entiteto hitbox.
     *
     * @return entiteto hitbox
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Grąžina animacijos indeksą.
     *
     * @return animacijos indeksas
     */
    public int getAniIndex() {
        return aniIndex;
    }
}
