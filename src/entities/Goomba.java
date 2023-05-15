package entities;

import gamestates.Playing;

import java.awt.*;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;
import static utils.HelpMethods.IsFloor;

public class Goomba extends Enemy{

    public Goomba(float x, float y) {
        super(x, y, GOOMBA_WIDTH, GOOMBA_HEIGHT, GOOMBA);
        initHitbox(x,y,GOOMBA_WIDTH,GOOMBA_HEIGHT);

    }

    public void update(int[][] levelData, Playing playing) {
        updateMove(levelData);
        updateAnimationTick();
        checkCollision(playing);
    }

    private void updateMove(int[][] levelData) {
        if(firstUpdate)
            firstUpdateCheck(levelData);

        if(inAir)
            updateInAir(levelData);
         else {
            switch (enemyState){
                case RUNNING:
                    move(levelData);
                    break;
            }
        }

    }
}
