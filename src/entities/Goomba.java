package entities;

import gamestates.Playing;
import static utils.Constants.EnemyConstants.*;

/**
 * Goomba klasė, skirta Goomba objektams.
 */
public class Goomba extends Enemy {

    /**
     * Sukuria naują Goomba objektą.
     *
     * @param x pradinė horizontalė pozicija
     * @param y pradinė vertikalė pozicija
     */
    public Goomba(float x, float y) {
        super(x, y, GOOMBA_WIDTH, GOOMBA_HEIGHT, GOOMBA);
        initHitbox(GOOMBA_WIDTH_DEFAULT, GOOMBA_HEIGHT_DEFAULT);
    }

    /**
     * Atnaujina Goomba objektą.
     *
     * @param levelData lygio duomenys
     * @param playing   esamasis žaidimo objektas
     */
    public void update(int[][] levelData, Playing playing) {
        updateMove(levelData);
        updateAnimationTick();
        checkCollision(playing);
    }

    /**
     * Atnaujina Goomba objekto judėjimą.
     *
     * @param levelData lygio duomenys
     */
    private void updateMove(int[][] levelData) {
        if (firstUpdate)
            firstUpdateCheck(levelData);

        if (inAir)
            updateInAir(levelData);
        else {
            switch (state) {
                case RUNNING:
                    move(levelData);
                    break;
            }
        }
    }
}
