package utils;

import entities.Goomba;
import main.Game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.CollisionConstants.*;
import static utils.Constants.Directions.LEFT;

/**
 * Šioje klasėje aprašomi pagalbiniai metodai, naudojami žaidime.
 */
public class HelpMethods {

    /**
     * Patikrina, ar galima judėti į norimą vietą.
     *
     * @param x          horizontali koordinatė
     * @param y          vertikali koordinatė
     * @param width      objekto plotis
     * @param height     objekto aukštis
     * @param levelData  lygio duomenys
     * @return true, jei galima judėti, false - jei ne
     */
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if(!IsSolid(x,y,levelData))
            if(!IsSolid(x+width,y+height,levelData))
                if(!IsSolid(x+width,y,levelData))
                    if(!IsSolid(x,y+height,levelData))
                        return true;
        return false;
    }

    /**
     * Patikrina, ar taškas yra kažkoks objektas.
     *
     * @param x          horizontali koordinatė
     * @param y          vertikali koordinatė
     * @param levelData  lygio duomenys
     * @return true, jei taškas yra kietas objektas, false - jei ne
     */
    private static boolean IsSolid(float x, float y, int[][] levelData) {
        int maxWidth = levelData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        if (yIndex < 0)
            return false;
        int value = levelData[(int)yIndex][(int)xIndex];

        if ((value >= 31 && value <= 42) || (value >= 20 && value <= 25) || (value >= 3 && value <= 10))
            return false;
        if (value >= 42 || value < 0 || value != 26)
            return true;

        return false;
    }

    /**
     * Patikrina, ar taškas yra pabaigos blokas.
     *
     * @param x          horizontali koordinatė
     * @param y          vertikali koordinatė
     * @param levelData  lygio duomenys
     * @return true, jei taškas yra pabaigos blokas, false - jei ne
     */
    public static boolean IsFinishTile(float x, float y, int[][] levelData) {
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        if (yIndex > 0) {
            int value = levelData[(int)yIndex][(int)xIndex];
            if (value == 40 || value == 41)
                return true;
        }
        return false;
    }

    /**
     * Grąžina horizontalią koordinatę, kuri yra greta sienos.
     *
     * @param hitbox  Susidūrimo dėžė
     * @param xSpeed  X greitis
     * @return horizontali koordinatė, kuri yra greta sienos
     */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = Math.round(hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE-hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * Grąžina vertikalų koordinatę, kuri yra virš stogo arba virš grindų.
     *
     * @param hitbox    Susidūrimo dėžė
     * @param airSpeed  Oro greitis
     * @return vertikalioji koordinatė, kuri yra virš stogo arba virš grindų
     */
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = Math.round(hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) { // Falling/touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE-hitbox.height);
            return tileYPos + yOffset - 1;
        } else { //jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * Patikrina, ar Entity yra ant grindų.
     *
     * @param hitbox     Susidūrimo dėžė
     * @param levelData  Lygio duomenys
     * @return true, jei entitetas yra ant grindų, kitaip false
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height+1, levelData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height+1, levelData))
            {
                return false;
            }
        return true;
    }

    /**
     * Patikrina, ar Entity sudaužo bloką.
     *
     * @param hitbox     Susidūrimo dėžė
     * @param levelData  Lygio duomenys
     * @return true, jei entitetas sudaužo bloką, kitaip false
     */
    public static boolean IsEntityHittingBlock(Rectangle2D.Float hitbox, int[][] levelData) {
        if (!IsSolid(hitbox.x, hitbox.y-1, levelData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y-1, levelData))
            {
//                System.out.println("Not hitting block in right corner");
                return false;
            }
        }
        System.out.println("Hitting block");
        return true;
    }

    /**
     * Grąžina tašką, kuriame Entity sudaužo bloką.
     *
     * @param hitbox     Susidūrimo dėžė
     * @param levelData  Lygio duomenys
     * @return taškas, kuriame entitetas sudaužo bloką
     */
    public static Point2D.Float GetHittingPos(Rectangle2D.Float hitbox, int[][] levelData) {
//        float xIndex = hitbox.x / Game.TILES_SIZE;
//        float yIndex = (hitbox.y-1) / Game.TILES_SIZE;
        if (IsSolid(hitbox.x, hitbox.y-1, levelData))
            return new Point2D.Float((float) Math.floor(hitbox.x/Game.TILES_SIZE), (float) (Math.floor((hitbox.y-1)/Game.TILES_SIZE)));
        else if (IsSolid(hitbox.x + hitbox.width, hitbox.y-1, levelData))
            return new Point2D.Float((float) (Math.floor(hitbox.x + hitbox.width)/Game.TILES_SIZE), (float) (Math.floor((hitbox.y-1)/Game.TILES_SIZE)));
        return null;
    }

    /**
     * Apverčia paveikslėlį horizontaliai arba vertikaliai.
     *
     * @param image       Paveikslėlis
     * @param horizontal  Horizontalus apvertimas
     * @param vertical    Vertikalus apvertimas
     * @return apverstas paveikslėlis
     */
    public static BufferedImage flipImage(final BufferedImage image, final boolean horizontal,
                                          final boolean vertical) {
        int x = 0;
        int y = 0;
        int w = image.getWidth();
        int h = image.getHeight();

        final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = out.createGraphics();

        if (horizontal) {
            x = w;
            w *= -1;
        }

        if (vertical) {
            y = h;
            h *= -1;
        }

        g2d.drawImage(image, x, y, w, h, null);
        g2d.dispose();

        return out;
    }

    /**
     * Tikrina, ar yra grindys po Entity.
     *
     * @param hitbox     Susidūrimo dėžė
     * @param xSpeed     X greitis
     * @param levelData  Lygio duomenys
     * @return true, jei yra grindys po entitetu, kitaip false
     */
    public static boolean  IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        if (xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, levelData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
    }

    /**
     * Tikrina, ar Entity yra lygio krašte.
     *
     * @param hitbox     Susidūrimo dėžė
     * @param xSpeed     X greitis
     * @param levelData  Lygio duomenys
     * @param dir        Kryptis
     * @return true, jei entitetas yra lygio krašte, kitaip false
     */
    public static boolean IsMapEdge(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData, int dir) {
//        System.out.println("IsMapEdge: " + (hitbox.x == 0 || hitbox.x + hitbox.width == levelData[0].length*Game.TILES_SIZE));
        return (hitbox.x == 0 && dir == LEFT) || hitbox.x + hitbox.width == levelData[0].length*Game.TILES_SIZE;
    }

    /**
     * Tikrina susidūrimo pusę tarp dėžės ir priešininko.
     *
     * @param hitbox       Dėžės susidūrimo dėžė
     * @param enemyHitbox  Priešininko susidūrimo dėžė
     * @return susidūrimo pusė: COLLISION_TOP, COLLISION_LEFT, COLLISION_RIGHT, COLLISION_BOTTOM arba COLLISION_NONE
     */
    public static int CheckCollisionSide(Rectangle2D.Float hitbox, Rectangle2D.Float enemyHitbox) {
        if (hitbox.intersects(enemyHitbox)) {
            float dx = (hitbox.x + hitbox.width / 2) - (enemyHitbox.x + enemyHitbox.width / 2);
            float dy = (hitbox.y + hitbox.height / 2) - (enemyHitbox.y + enemyHitbox.height / 2);
            float width = (hitbox.width + enemyHitbox.width) / 2;
            float height = (hitbox.height + enemyHitbox.height) / 2;
            float crossWidth = width * dy;
            float crossHeight = height * dx;

            if (Math.abs(dx) <= width && Math.abs(dy) <= height) {
                if (crossWidth > -crossHeight) {
                    if (crossWidth > crossHeight) {
                        return COLLISION_TOP;
                    } else {
                        return COLLISION_LEFT;
                    }
                } else {
                    if (crossWidth > crossHeight) {
                        return COLLISION_RIGHT;
                    } else {
                        return COLLISION_BOTTOM;
                    }
                }
            }
        }
        return COLLISION_NONE;
    }

    /**
     * Gauti lygio duomenis iš paveikslėlio.
     *
     * @param img  Paveikslėlis
     * @return lygio duomenys
     */
    public static int[][] GetLevelData(BufferedImage img) {
        int[][] levelData = new int[img.getHeight()][img.getWidth()];

        for (int i = 0; i < img.getHeight(); i++)
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                levelData[i][j] = value;
            }
        return levelData;
    }

    /**
     * Gauti goombas iš paveikslėlio.
     *
     * @param img  Paveikslėlis
     * @return goombų sąrašas
     */
    public static ArrayList<Goomba> GetGoombas(BufferedImage img) {
        ArrayList<Goomba> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++)
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == 0)
                    list.add(new Goomba(j* Game.TILES_SIZE, i*Game.TILES_SIZE));
            }
        return list;
    }

    /**
     * Gauti žaidėjo atsiradimo tašką iš paveikslėlio.
     *
     * @param img  Paveikslėlis
     * @return žaidėjo atsiradimo taškas
     */
    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++)
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == 101)
                    return new Point(j*Game.TILES_SIZE, i*Game.TILES_SIZE);
            }
        return new Point(200 * Game.TILES_SIZE, 200 * Game.TILES_SIZE);
    }
}
