package gamestates;

import main.Game;
import main.GamePanel;
import ui.IOButton;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class IO extends State implements Statemethods {

    private IOButton[] buttons = new IOButton[2];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;
    private Game game;

    public IO(Game game) {
        super(game);
        this.game = game;
        loadButtons();
        loadBackground();

    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.SAVE_LOAD_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH/2 - menuWidth/2;
        menuY = (int) (45*Game.SCALE);

    }

    private void loadButtons() {
        buttons[0] = new IOButton(Game.GAME_WIDTH/2, (int) (180 * Game.SCALE), 0, Gamestate.SAVE, game);
        buttons[1] = new IOButton(Game.GAME_WIDTH/2, (int) (250 * Game.SCALE), 1, Gamestate.LOAD, game);
    }

    @Override
    public void update() {
        for (IOButton ib : buttons) {
            ib.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (IOButton ib : buttons) {
            ib.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (IOButton ib : buttons) {
            if(isIn(e,ib)) {
                ib.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (IOButton ib : buttons) {
            if(isIn(e,ib)) {
                if(ib.isMousePressed())
                    ib.workWithData();
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (IOButton ib : buttons)
            ib.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (IOButton ib : buttons) {
            ib.setMouseOver(false);
        }
        for (IOButton ib : buttons) {
            if (isIn(e,ib)) {
                ib.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
