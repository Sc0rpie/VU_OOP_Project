package inputs;

import gamestates.Gamestate;
import main.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Pelės įvesties klasė
 */
public class MouseInputs implements MouseListener, MouseMotionListener {

    /**
     * Žaidimo panelis
     * @param gamePanel žaidimo panelis
     */
    private GamePanel gamePanel;

    /**
     * Klasės konstruktorius
     * @param gamePanel žaidimo panelis
     */
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Metodas, kuris atlieka veiksmus, kai paspaudžiamas pelės mygtukas
     * @param e pelės mygtuko paspaudimas
     */
    @Override
    public void mouseClicked(MouseEvent e) {
       switch(Gamestate.state) {
           case MENU:
               gamePanel.getGame().getMenu().mouseClicked(e);
               break;
           case PLAYING:
               gamePanel.getGame().getPlaying().mouseClicked(e);
               break;
       }
    }

    /**
     * Metodas, kuris atlieka veiksmus, kai paspaudžiamas pelės mygtukas
     * @param e pelės mygtuko paspaudimas
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
        }
    }

    /**
     * Metodas, kuris atlieka veiksmus, kai atleidžiamas pelės mygtukas
     * @param e pelės mygtuko atleidimas
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Metodas, kuris atlieka veiksmus, kai pelė judinama
     * @param e pelės mygtuko judinimo event'as
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
        }
    }
}
