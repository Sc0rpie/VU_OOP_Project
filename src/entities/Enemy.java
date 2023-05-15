package entities;

import gamestates.Playing;
import main.Game;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

public abstract class Enemy extends Entity{
    protected int aniIndex, enemyType, enemyState;
    protected int aniTick, aniSpeed = 50;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed, gravity = 0.05f * Game.SCALE;
    protected float walkSpeed = 0.25f * Game.SCALE;
    protected int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    protected void firstUpdateCheck(int[][] levelData) {
        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] levelData) {
        if(CanMoveHere(hitbox.x, hitbox.y+ fallSpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
        }
    }

    protected void move(int[][] levelData) {
        float xSpeed;
        if(walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData))
            if (!IsMapEdge(hitbox, xSpeed, levelData, walkDir))
                if(IsFloor(hitbox, xSpeed, levelData)) {
                    hitbox.x += xSpeed;
                    return;
                }
        changeWalkDir();
    }

    protected void newState (int enemyState) {
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, enemyState))
                aniIndex = 0;
        }
    }

    protected void checkCollision(Playing playing) {
        if (DidCollideWithEnemy(playing.getPlayer().getHitbox(), hitbox))
            playing.getPlayer().setDead(true);

    }

    protected void changeWalkDir() {
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
