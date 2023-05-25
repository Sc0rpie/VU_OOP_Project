package gamestates;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Meniu klasė.
 * Ši klasė savyje saugo meniu mygtukus ir foną.
 */
public class Menu extends State implements Statemethods {

    /**
     * Meniu mygtukų masyvas.
     */
    private MenuButton[] buttons = new MenuButton[2];
    /**
     * Meniu fonas.
     */
    private BufferedImage backgroundImg;
    /**
     * Meniu koordinatės ir dydis.
     */
    private int menuX, menuY, menuWidth, menuHeight;

    /**
     * Meniu konstruktorius.
     * Jame yra pakraunami meniu mygtukai ir fonas.
     * @param game Žaidimo klasė.
     */
    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    /**
     * Meniu fonų pakrovimo metodas bei pozicijos nustatymas.
     */
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH/2 - menuWidth/2;
        menuY = 0;
    }

    /**
     * Meniu mygtukų pakrovimo metodas.
     */
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH/2, (int) (100 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2, (int) (160 * Game.SCALE), 2, Gamestate.QUIT);
    }

    /**
     * Meniu mygtukų atnaujinimai.
     */
    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    /**
     * Meniu mygtukų piešimas.
     * @param g Grafinis objektas.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Patikrina ar pelės mygtukas buvo paspaustas
     * @param e Pelės eventas
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if(isIn(e,mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
     }

    /**
     * Patikrina ar pelės mygtukas buvo atleistas
     * @param e Pelės eventas
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if(isIn(e,mb)) {
                if(mb.isMousePressed())
                    mb.applyGamestate();
                break;
            }
        }
        resetButtons();
    }

    /**
     * Grąžina visas mygtukų būsenas į pradinę.
     */
    private void resetButtons() {
        for (MenuButton mb : buttons)
            mb.resetBools();
    }

    /**
     * Patikrina ar pelė buvo judinta
     * @param e Pelės eventas
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }
        for (MenuButton mb : buttons) {
            if (isIn(e,mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    /**
     * Patikrina ar klaviaturos mygtukas buvo paspaustas
     * @param e Klaviaturos eventas
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
