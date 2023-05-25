package inputs;

import gamestates.Gamestate;
import main.Game;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static utils.Constants.Directions.*;

/**
 * Klaviatūros įvesties klasė
 */
public class KeyboardInputs implements KeyListener {

    /**
     * Žaidimo panelis
     * @param gamePanel žaidimo panelis
     */
    private GamePanel gamePanel;

    /**
     * Klasės konstruktorius
     * @param gamePanel žaidimo panelis
     */
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Metodas, kuris atlieka veiksmus, kai paspaudžiamas klavišas
     * @param e klavišo paspaudimas
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
        }
    }

    /**
     * Metodas, kuris atlieka veiksmus, kai atleidžiamas klavišas
     * @param e klavišo atleidimas
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
        }
    }
}
