package utils;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
//        System.out.println("Is solid value: " + value);
//        System.out.println("Value: " + value);
        if ((value >= 31 && value <= 39) || (value >= 20 && value <= 25))
            return false;
        if (value >= 40 || value < 0 || value != 26)
        {
//            System.out.println("Is solid: true");
            return true;
        }

        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = Math.round(hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
//            System.out.println("xSpeed > 0");
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
//            int tileYPos = currentTile * Game.TILES_SIZE;
//            int yOffset = (int)(Game.TILES_SIZE-hitbox.height);
//            return tileYPos + yOffset - 1;
            return currentTile * Game.TILES_SIZE;
        }

    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height+1, levelData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height+1, levelData))
            {
//                System.out.println("IsEntityOnFloor: false");
                return false;
            }
//        System.out.println("IsEntityOnFloor: true");
        return true;
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
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
    }
    public static boolean IsMapEdge(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData, int dir) {
//        System.out.println("IsMapEdge: " + (hitbox.x == 0 || hitbox.x + hitbox.width == levelData[0].length*Game.TILES_SIZE));
        return (hitbox.x == 0 && dir == LEFT) || hitbox.x + hitbox.width == levelData[0].length*Game.TILES_SIZE;
    }
    public static boolean DidCollideWithEnemy(Rectangle2D.Float hitbox, Rectangle2D.Float enemyHitbox) {
        return hitbox.intersects(enemyHitbox);
    }
}
