package gamestates;

import entities.EnemyHandler;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utils.HelpMethods.IsFinishTile;

/**
 * Žaidimo būsena, kuri vykdoma žaidimo metu.
 */
public class Playing extends State implements Statemethods{

    /**
     * Žaidėjo objektas.
     */
    private Player player;
    /**
     * Lygio valdymo objektas.
     */
    private LevelHandler levelHandler;
    /**
     * Priešų valdymo objektas.
     */
    private EnemyHandler enemyHandler;
    /**
     * Lygio baigimo objektas.
     */
    private LevelCompletedOverlay levelCompletedOverlay;
    /**
     * Game over objektas.
     */
    private GameOverOverlay gameOverOverlay;

    /**
     * Lygio horizontalus offsetas.
     */
    private int xLevelOffset;
    /**
     * Lygio kairiojo krašto offset'as.
     */
    private int leftBorder = (int) (0.3 * Game.GAME_WIDTH);
    /**
     * Lygio dešiniojo krašto offset'as.
     */
    private int rightBorder = (int) (0.6 * Game.GAME_WIDTH);
    /**
     * Lygio maksimalus offset'as.
     */
    private int maxLevelOffsetX;
    /**
     * Ar rodyti lygio baigimo overlay.
     */
    private boolean showLevelCompletedOverlay = false;
    /**
     * Ar rodyti game over overlay.
     */
    private boolean showGameOverOverlay = false;

    /**
     * Žaidimo būsenos konstruktorius.
     * @param game Žaidimo objektas.
     */
    public Playing(Game game) {
        super(game);
        initClasses();
    }

    /**
     * Inicializuojami klasės kintamieji, kurie yra naudojami žaidimo metu.
     */
    private void initClasses() {
        levelHandler = new LevelHandler(game);
        enemyHandler = new EnemyHandler(this);
        player = new Player(200, 200, (int)(16*Game.SCALE), (int)(16*Game.SCALE));
        player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
        player.setSpawn(levelHandler.getCurrentLevel().getPlayerSpawn());
        calculateLevelOffset();
        loadStartLevel();
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
    }

    /**
     * Metodas, pakraunantis kitą lygį.
     */
    public void loadNextLevel() {
        resetAll();
        levelHandler.loadNextLevel();
        player.setSpawn(levelHandler.getCurrentLevel().getPlayerSpawn());
    }

    /**
     * Metodas, pakraunantis pradinį lygį.
     */
    private void loadStartLevel() {
        enemyHandler.loadEnemies(levelHandler.getCurrentLevel());
    }

    /**
     * Metodas, kuris apskaičiuoja lygio offset'ą.
     */
    private void calculateLevelOffset() {
        maxLevelOffsetX = levelHandler.getCurrentLevel().getLevelOffset();
    }

    @Override
    public void update() {
        if (showLevelCompletedOverlay) {
            levelCompletedOverlay.update();
        } else if (!player.isDead()) {
            levelHandler.update();
            player.update();
            enemyHandler.update(levelHandler.getCurrentLevel().getLevelData());
            checkCloseToBorder();
            checkDeathPits();
            if (!player.getFinishState())
                checkIfPlayerFinished();
            checkIfRendering();
        } else {
            player.update();
//            System.out.println(player.getRestartRequest());
            if (player.getRestartRequest()) {
                System.out.println("Restarting");
                showGameOverOverlay = true;
                gameOverOverlay.update();
            }
        }
    }

    private void checkDeathPits() {
        if (player.getHitbox().y + player.getHitbox().height + 1 >= Game.GAME_HEIGHT)
            player.setDead(true);
    }

    private void checkIfRendering() {
        if (!player.isRendering())
            showLevelCompletedOverlay = true;
    }

    public void resetAll() {
        finishLevel(false);
        player.setRendering(true);
        showLevelCompletedOverlay = false;
        showGameOverOverlay = false;
        player.resetAll();
        levelHandler.restartLevelTiles();
        enemyHandler.resetAllEnemies();
        System.out.println(levelHandler.getCurrentLevel());
        enemyHandler.loadEnemies(levelHandler.getCurrentLevel());
    }

    private void checkIfPlayerFinished() {
        if (IsFinishTile(player.getHitbox().x, player.getHitbox().y, levelHandler.getCurrentLevel().getLevelData()))
            finishLevel(true);
    }

    private void finishLevel(boolean finished) {
        player.setFinishState(finished);
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLevelOffset;

        if (diff > rightBorder)
            xLevelOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLevelOffset += diff - leftBorder;

        if(xLevelOffset > maxLevelOffsetX)
            xLevelOffset = maxLevelOffsetX;
        else if (xLevelOffset < 0)
            xLevelOffset = 0;
    }

    /**
     * Žaidimo piešimo metodas.
     * @param g Grafinis objektas.
     */
    @Override
    public void draw(Graphics g) {
        levelHandler.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyHandler.draw(g, xLevelOffset);
        if (showGameOverOverlay)
            gameOverOverlay.draw(g);
        if (showLevelCompletedOverlay)
            levelCompletedOverlay.draw(g);
    }

    /**
     * Metodas, kuris nustato maksimalų lygio offset'ą.
     * @param levelOffset Lygio offset'as.
     */
    public void setMaxLevelOffset (int levelOffset) {
        this.maxLevelOffsetX = levelOffset;
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
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttacking(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * Patikrina ar klaviaturos mygtukas buvo paspaustas
     * @param e Klaviaturos eventas
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
                break;
            case KeyEvent.VK_ENTER:
                if (showLevelCompletedOverlay)
                    loadNextLevel();
                else if (showGameOverOverlay)
                    resetAll();
        }
    }

    /**
     * Patikrina ar klaviaturos mygtukas buvo atleistas
     * @param e Klaviaturos eventas
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    /**
     * Metodas "player" objekto gavimui.
     * @return player Player objektas.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Metodas "enemyHandler" objekto gavimui.
     * @return enemyHandler EnemyHandler objektas.
     */
    public EnemyHandler getEnemyHandler() {
        return enemyHandler;
    }

    /**
     * Metodas žaidimo pabaigos overlay'o rodymo nustatymui
     * @param showLevelCompletedOverlay Ar rodyti overlay'ą
     * @return showLevelCompletedOverlay Ar rodomas overlay'as
     */
    public boolean setShowLevelCompletedOverlay(boolean showLevelCompletedOverlay) {
        return this.showLevelCompletedOverlay = showLevelCompletedOverlay;
    }
}
