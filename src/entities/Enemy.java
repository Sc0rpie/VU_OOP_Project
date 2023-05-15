package entities;

import main.Game;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

public abstract class Enemy extends Entity{
    private int aniIndex, enemyType, enemyState;
    private int aniTick, aniSpeed = 50;
    private boolean firstUpdate = true;
    private boolean inAir;
    private float fallSpeed, gravity = 0.05f * Game.SCALE;
    private float walkSpeed = 0.25f * Game.SCALE;
    private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, enemyState))
                aniIndex = 0;
        }
    }

    public void update(int[][] levelData) {
        updateMove(levelData);
        updateAnimationTick();
    }

    private void updateMove(int[][] levelData) {
        if(firstUpdate) {
            if (!IsEntityOnFloor(hitbox, levelData))
                inAir = true;
            firstUpdate = false;
        }

        if(inAir) {
            if(CanMoveHere(hitbox.x, hitbox.y+ fallSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        } else {
//            System.out.println("Enemy state: " + enemyState);
            switch (enemyState){
                case RUNNING:
                    float xSpeed;

                    if(walkDir == LEFT)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;

                    System.out.println("Goomba xPos: " + hitbox.x);
//                    System.out.println("xSpeed: " + xSpeed);
                    if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData))
                        if (!IsMapEdge(hitbox, xSpeed, levelData, walkDir))
                            if(IsFloor(hitbox, xSpeed, levelData)) {
                                hitbox.x += xSpeed;
                                return;
                        }
//                    System.out.println("Change walk dir");
                    changeWalkDir();
                    break;
            }
        }

    }

    private void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
