package main;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;

/**
 * Pagrindinė žaidimo klasė, kuri valdo visas kitas klases.
 */
public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;

    /**
     * Pagrindinės žaidimo konstantos.
     */
    public final static int TILES_DEFAULT_SIZE = 16;
    public final static float SCALE = 3f;
    public final static int TILES_IN_WIDTH = 16;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    /**
     * Konstruktorius, kuris inicializuoja žaidimo langą, panelę ir pradeda žaidimo ciklą.
     */
    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    /**
     * Inicializuoja visas kitas reikalingas klases.
     */
    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    /**
     * Pradeda žaidimo ciklą.
     */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    public void update() {
        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    /**
     * Atvaizduoja žaidimą.
     *
     * @param g Grafika
     */
    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }

    /**
     * Metodas skirtas žaidimo ciklui.
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastChecked = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while(true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if(System.currentTimeMillis() - lastChecked >= 1000) {
                lastChecked = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    /**
     * Metodas skirtas tikrinimui, ar žaidimas yra žaidžiamas.
     */
    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    /**
     * Gauna menių objektą
     * @return meniu objektas
     */
    public Menu getMenu() {
        return menu;
    }
    /**
     * Gauna Playing objektą
     * @return Playing objektas
     */
    public Playing getPlaying() {
        return playing;
    }
}
