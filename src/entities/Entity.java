package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x,y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed = 0f;
    protected boolean inAir = false;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width=width;
        this.height=height;
    }

    protected void drawHitbox(Graphics g, int xLevelOffset) {
        //Hitbox debug
        g.setColor(Color.RED);
        g.drawRect((int)hitbox.x - xLevelOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}
