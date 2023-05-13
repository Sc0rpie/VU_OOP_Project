package levels;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelHandler {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelHandler (Game game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

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

    public void draw (Graphics g, int levelOffset) {
        g.setColor(new Color(92,148,252));
        g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++)
            for (int j = 0; j < levelOne.getLevelData()[0].length; j++){
                int index = levelOne.getSpriteIndex(j,i);
                g.drawImage(levelSprite[index], Game.TILES_SIZE*j - levelOffset, Game.TILES_SIZE*i, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }

    }

    public void update () {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }
}
