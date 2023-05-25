package levels;

import entities.Goomba;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.HelpMethods.GetLevelData;
import static utils.HelpMethods.GetGoombas;
import static utils.HelpMethods.GetPlayerSpawn;

/**
 * Klasė, kuri aprašo lygį.
 */
public class Level {

    private BufferedImage img;
    private int[][] levelData;
    private ArrayList<Goomba> goombas;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    /**
     * Konstruktorius su lygio atvaizdu.
     *
     * @param img Lygio atvaizdas
     */
    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calculateLevelOffsets();
        calculatePlayerSpawn();
    }

    /**
     * Žaidėjo atsiradimo vietos apskaičiavimas.
     */
    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    /**
     * Žaidimo lygio sudarymas.
     */
    private void createLevelData() {
        levelData = GetLevelData(img);
    }

    /**
     * Goombos objektų sąrašo sudarymas.
     */
    private void createEnemies() {
        goombas = GetGoombas(img);
    }

    /**
     * Atstatyti lygio plyteles.
     */

    protected void restartLevelTiles() {
        levelData = GetLevelData(img);
    }

    /**
     * Lygio poslinkio apskaičiavimas.
     */
    private void calculateLevelOffsets() {
        levelTilesWide = levelData[0].length;
        maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    /**
     * Gauti plytelės indeksą pagal koordinates.
     *
     * @param x Plytelės x koordinatė
     * @param y Plytelės y koordinatė
     * @return Plytelės indeksas
     */
    public int getSpriteIndex(int x, int y){
        return levelData[y][x];
    }

    /**
     * Nustatyt plytelės indeksą pagal koordinates.
     *
     * @param x Plytelės x koordinatė
     * @param y Plytelės y koordinatė
     * @param index Plytelės indeksas
     */
    public void setSpriteIndex(int x, int y, int index){
        levelData[y][x] = index;
    }

    /**
     * Metodas lygio duomenų gavimui.
     *
     * @return Lygio duomenys
     */
    public int[][] getLevelData() {
        return levelData;
    }

    /**
     * Lygio poslinkio gavimas.
     *
     * @return Lygio poslinkis
     */
    public int getLevelOffset() {
        return maxLevelOffsetX;
    }

    /**
     * Goombos objektų sąrašo gavimas.
     *
     * @return Goombos objektų sąrašas
     */
    public ArrayList<Goomba> getGoombas() {
        return goombas;
    }

    /**
     * Žaidėjo atsiradimo vietos gavimas.
     *
     * @return Žaidėjo iššokimo vieta
     */
    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
