package gamestates;

import main.Game;
import ui.IOButton;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;
    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    public boolean isIn(MouseEvent e, IOButton ib) {
        return ib.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }
}
