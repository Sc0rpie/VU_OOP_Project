package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Žaidimo būsenos metodų interfeisas.
 */
public interface Statemethods {
    /**
     * Metodas, kuris atnaujina žaidimą.
     */
    void update();
    /**
     * Metodas, kuris piešia žaidimą.
     * @param g Grafinis objektas.
     */
    void draw(Graphics g);
    /**
     * Metodas, kuris apdoroja pelės paspaudimus.
     * @param e Pelės event'as.
     */
    void mouseClicked(MouseEvent e);
    /**
     * Metodas, kuris apdoroja pelės paspaudimą.
     * @param e Pelės event'as.
     */
    void mousePressed(MouseEvent e);
    /**
     * Metodas, kuris apdoroja pelės atleidimą.
     * @param e Pelės event'as.
     */
    void mouseReleased(MouseEvent e);
    /**
     * Metodas, kuris apdoroja pelės judėjimą.
     * @param e Pelės event'as.
     */
    void mouseMoved(MouseEvent e);
    /**
     * Metodas, kuris apdoroja klaviatūros klavišo paspaudimą.
     * @param e Klaviatūros event'as.
     */
    void keyPressed(KeyEvent e);
    /**
     * Metodas, kuris apdoroja klaviatūros klavišo atleidimą.
     * @param e Klaviatūros event'as.
     */
    void keyReleased(KeyEvent e);
}
