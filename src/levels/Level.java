package levels;

import entities.Goomba;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.HelpMethods.GetLevelData;
import static utils.HelpMethods.GetGoombas;
import static utils.HelpMethods.GetPlayerSpawn;

public class Level {

    private BufferedImage img;
    private int[][] levelData;
    private ArrayList<Goomba> goombas;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calculateLevelOffsets();
        calculatePlayerSpawn();
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void createLevelData() {
        levelData = GetLevelData(img);
    }

    private void createEnemies() {
        goombas = GetGoombas(img);
    }

    protected void restartLevelTiles() {
        levelData = GetLevelData(img);
    }

    private void calculateLevelOffsets() {
        levelTilesWide = levelData[0].length;
        maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y){
        return levelData[y][x];
    }

    public void setSpriteIndex(int x, int y, int index){
        levelData[y][x] = index;
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public int getLevelOffset() {
        return maxLevelOffsetX;
    }

    public ArrayList<Goomba> getGoombas() {
        return goombas;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
