package ui;

import gamestates.Gamestate;
import utils.LoadSave;
import static utils.Constants.UI.Buttons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasė, atsakinga už meniu mygtukų rodymą ir veiksmus.
 */
public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    /**
     * Konstruktorius, kuris sukuria meniu mygtuką.
     *
     * @param xPos      mygtuko x koordinatė
     * @param yPos      mygtuko y koordinatė
     * @param rowIndex  eilutės indeksas, kuriame yra mygtukas
     * @param state     mygtuko susietas žaidimo būsenos objektas
     */
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    /**
     * Piešia meniu mygtuką.
     *
     * @param g grafinis objektas
     */
    public void draw (Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    /**
     * Atnaujina meniu mygtuką.
     */
    public void update() {
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Taiko susietą žaidimo būseną.
     */
    public void applyGamestate() {
        Gamestate.state = state;
    }

    /**
     * Atstatomi mygtuko būsenos kintamieji.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
