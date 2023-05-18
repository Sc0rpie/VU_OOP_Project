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

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelHandler levelHandler;
    private EnemyHandler enemyHandler;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameOverOverlay gameOverOverlay;

    private int xLevelOffset;
    private int leftBorder = (int) (0.3 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.6 * Game.GAME_WIDTH);
    private int maxLevelOffsetX;
    private boolean showLevelCompletedOverlay = false;
    private boolean showGameOverOverlay = false;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

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

    public void loadNextLevel() {
        resetAll();
        levelHandler.loadNextLevel();
        player.setSpawn(levelHandler.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyHandler.loadEnemies(levelHandler.getCurrentLevel());
    }

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

    public void setMaxLevelOffset (int levelOffset) {
        this.maxLevelOffsetX = levelOffset;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

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

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyHandler getEnemyHandler() {
        return enemyHandler;
    }

    public boolean setShowLevelCompletedOverlay(boolean showLevelCompletedOverlay) {
        return this.showLevelCompletedOverlay = showLevelCompletedOverlay;
    }
}
