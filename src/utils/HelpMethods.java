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

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if(!IsSolid(x,y,levelData))
            if(!IsSolid(x+width,y+height,levelData))
                if(!IsSolid(x+width,y,levelData))
                    if(!IsSolid(x,y+height,levelData))
                        return true;
        return false;
    }

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

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height+1, levelData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height+1, levelData))
            {
                return false;
            }
        return true;
    }

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

    public static Point2D.Float GetHittingPos(Rectangle2D.Float hitbox, int[][] levelData) {
//        float xIndex = hitbox.x / Game.TILES_SIZE;
//        float yIndex = (hitbox.y-1) / Game.TILES_SIZE;
        if (IsSolid(hitbox.x, hitbox.y-1, levelData))
            return new Point2D.Float((float) Math.floor(hitbox.x/Game.TILES_SIZE), (float) (Math.floor((hitbox.y-1)/Game.TILES_SIZE)));
        else if (IsSolid(hitbox.x + hitbox.width, hitbox.y-1, levelData))
            return new Point2D.Float((float) (Math.floor(hitbox.x + hitbox.width)/Game.TILES_SIZE), (float) (Math.floor((hitbox.y-1)/Game.TILES_SIZE)));
        return null;
    }

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
    public static boolean  IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        if (xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, levelData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
    }
    public static boolean IsMapEdge(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData, int dir) {
//        System.out.println("IsMapEdge: " + (hitbox.x == 0 || hitbox.x + hitbox.width == levelData[0].length*Game.TILES_SIZE));
        return (hitbox.x == 0 && dir == LEFT) || hitbox.x + hitbox.width == levelData[0].length*Game.TILES_SIZE;
    }
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
