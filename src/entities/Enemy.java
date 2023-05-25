package entities;

import gamestates.Playing;
import main.Game;

import static utils.Constants.CollisionConstants.*;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;

/**
 * Enemy klasė suteikianti bendrą funkcionalumą priešų objektams.
 * Ji paveldi Entity klasę ir suteikia bendrą funkcionalumą priešų objektams.
 */
public abstract class Enemy extends Entity {

    /**
     * Priešo tipas.
     */
    protected int enemyType;
    /**
     * Animacijos greitis (didesnis skaičius = lėtesnė animacija).
     */
    protected int aniSpeed = 50;
    /**
     * Pirmasis atnaujinimas (tikrinama ar priešas yra ore).
     */
    protected boolean firstUpdate = true;
    /**
     * Vaikščiojimo greitis.
     */
    protected float walkSpeed = 0.25f * Game.SCALE;
    /**
     * Vaikščiojimo kryptis.
     */
    protected int walkDir = LEFT;

    /**
     * Sukuria naują Enemy objektą su nurodytomis pozicijomis, matmenimis ir priešo tipu.
     *
     * @param x         Priešo x koordinatė.
     * @param y         Priešo y koordinatė.
     * @param width     Priešo hitbox plotis.
     * @param height    Priešo hitbox aukštis.
     * @param enemyType Priešo tipas.
     */
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
    }

    /**
     * Patikrina, ar tai pirmas atnaujinimas priešui ir tinkamai nustato inAir kintamąjį.
     *
     * @param levelData Žaidimo pasaulio duomenys.
     */
    protected void firstUpdateCheck(int[][] levelData) {
        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
        firstUpdate = false;
    }

    /**
     * Atnaujina priešo padėtį ir elgesį, kai jis yra ore.
     *
     * @param levelData Žaidimo pasaulio duomenys.
     */
    protected void updateInAir(int[][] levelData) {
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
        }
    }

    /**
     * Judina priešą horizontaliai žaidimo pasaulyje.
     *
     * @param levelData Žaidimo pasaulio duomenys.
     */
    protected void move(int[][] levelData) {
        float xSpeed;
        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData))
            if (!IsMapEdge(hitbox, xSpeed, levelData, walkDir))
                if (IsFloor(hitbox, xSpeed, levelData)) {
                    hitbox.x += xSpeed;
                    return;
                }
        changeWalkDir();
    }

    /**
     * Nustato naują priešo būseną.
     *
     * @param state Nauja priešo būsena.
     */
    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Atnaujina animacijos laikmačio reikšmę ir keičia animacijos indeksą.
     */
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, state))
                aniIndex = 0;
        }
    }

    /**
     * Patikrina susidūrimą su žaidėju ir vykdo atitinkamus veiksmus.
     *
     * @param playing Esamo žaidimo būsenos objektas.
     */
    protected void checkCollision(Playing playing) {
        if (state == DEAD)
            return;
        int colType = CheckCollisionSide(playing.getPlayer().getHitbox(), hitbox); // gauti susidūrimo tipą
        if (colType == COLLISION_BOTTOM) {
            state = DEAD;
            playing.getPlayer().jumpUponKill();
        } else if (colType == COLLISION_LEFT || colType == COLLISION_RIGHT)
            playing.getPlayer().setDead(true); // nužudo žaidėją
    }

    /**
     * Keičia priešo judėjimo kryptį.
     */
    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    /**
     * Atstatyti priešo pradinę padėtį ir būseną.
     */
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        newState(RUNNING);
        airSpeed = 0;
    }

    /**
     * Grąžina priešo būseną.
     *
     * @return Priešo būsena.
     */
    public int getEnemyState() {
        return state;
    }
}
