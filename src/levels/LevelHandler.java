package levels;

import entities.Player;
import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.GetHittingPos;
import static utils.HelpMethods.IsEntityHittingBlock;

/**
 * Klasė, kuri valdo lygius.
 */
public class LevelHandler {

    private Game game;
    private Player player;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;
    private int aniTick, aniSpeed = 35, aniIndex = 13;
    private Point2D point;

    /**
     * Lygių valdiklio konstruktorius.
     * @param game Žaidimo objektas
     */
    public LevelHandler (Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    /**
     * Naujo lygio pakrovimas.
     */
    public void loadNextLevel() {
        levelIndex++;
        game.getPlaying().setShowLevelCompletedOverlay(false);
        if (levelIndex >= levels.size()) {
            levelIndex = 0;
            System.out.println("No more levels to load!");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(levelIndex);
        game.getPlaying().getEnemyHandler().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getLevelOffset());
    }

    /**
     * Visų lygių pakrovimas.
     */
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    /**
     * Atstatyti lygio plyteles.
     */
    public void restartLevelTiles() {
        levels.get(levelIndex).restartLevelTiles();
    }

    /**
     * Grafinių detalių pakrovimas iš failo.
     */
    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[45];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 9; j++) {
                int index = i*9+j;
                System.out.println("Index: " + index);
                levelSprite[index] = img.getSubimage(j*16, i*16, 16, 16);
            }
    }

    /**
     * Nupiešia lygį.
     *
     * @param g Grafinis objektas, skirtas piešimui
     * @param levelOffset Lygio poslinkis
     */
    public void draw (Graphics g, int levelOffset) {
        g.setColor(new Color(92,148,252));
        g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++)
            for (int j = 0; j < levels.get(levelIndex).getLevelData()[0].length; j++){
                int index = levels.get(levelIndex).getSpriteIndex(j,i);
                updateTileAnimation(index, j, i);
                g.drawImage(levelSprite[index], Game.TILES_SIZE*j - levelOffset, Game.TILES_SIZE*i, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }

    }

    private void updateTileAnimation(int index, int x, int y) {
        if (index >= 13 && index <= 15) {
            if (index == 15)
                levels.get(levelIndex).setSpriteIndex(x,y, aniIndex);
            else
                levels.get(levelIndex).setSpriteIndex(x,y, aniIndex);
        }
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 16) {
                aniIndex = 13;
            }
        }
    }

    /**
     * Atnaujina animacijos laikmačio reikšmę ir tikrina susidūrimus.
     */
    public void update () {
        updateAnimationTick();
        checkForCollisions();
    }

    /**
     * Tikrina susidūrimus.
     */
    private void checkForCollisions() {
        if (IsEntityHittingBlock(game.getPlaying().getPlayer().getHitbox(), levels.get(levelIndex).getLevelData())) {
            point = GetHittingPos(game.getPlaying().getPlayer().getHitbox(), levels.get(levelIndex).getLevelData());
            int value = levels.get(levelIndex).getLevelData()[(int)point.getY()][(int)point.getX()];
            if (value >= 13 && value <= 15) {
                levels.get(levelIndex).setSpriteIndex((int) Math.floor(point.getX()), (int)Math.floor(point.getY()), 16);
            }
            if (value == 1)
                levels.get(levelIndex).setSpriteIndex((int) Math.floor(point.getX()), (int)Math.floor(point.getY()), 26);
        }
    }

    /**
     * Gauna esamą lygį.
     *
     * @return Esamas lygis
     */
    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    /**
     * Gauna lygių skaičių.
     *
     * @return Lygių skaičius
     */
    public int getAmountOfLevels() {
        return levels.size();
    }
}
