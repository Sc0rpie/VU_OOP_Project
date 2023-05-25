package main;

import javax.swing.JPanel;
import java.awt.*;

import inputs.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

/**
 * Klasė, kuri atvaizduoja žaidimo panelę.
 */
public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;

    /**
     * Konstruktorius, kuris sukuria žaidimo panelę ir prideda įvykių klausytojus.
     *
     * @param game Žaidimo objektas
     */
    public GamePanel(Game game){
        mouseInputs = new MouseInputs(this);
        this.game = game;

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    /**
     * Nustato žaidimo panelės dydį.
     */
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("Size: " + GAME_WIDTH + " | " + GAME_HEIGHT);
    }

    /**
     * Nupiešia komponentą.
     *
     * @param g Grafikos objektas
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
    }

    /**
     * Gauna žaidimo objektą.
     *
     * @return Žaidimo objektas
     */
    public Game getGame() {
        return game;
    }

}
