package gamestates;

import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

/**
 * Žaidimo būsenos klasė.
 */
public class State {

    /**
     * Žaidimo objektas.
     */
    protected Game game;

    /**
     * Būsenos konstruktorius.
     * Nustato žaidimo objektą.
     * @param game Žaidimo objektas.
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Metodas, kuris patikrina ar pelės koordinatės yra meniu mygtuko ribose.
     * @param e Pelės koordinatės.
     * @param mb Meniu mygtukas.
     * @return Ar pelės koordinatės yra meniu mygtuko ribose.
     */
    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Metodas, leižiantis gauti žaidimo objektą.
     * @return game Žaidimo objektas.
     */
    public Game getGame() {
        return game;
    }
}
