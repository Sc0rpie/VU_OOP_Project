package utils;

import main.Game;

/**
 * Klasė, sauganti konstantas, kurios naudojamos žaidime.
 */
public class Constants {

    public static final float GRAVITY = 0.045f * Game.SCALE;

    /**
     * Klasė, sauganti konstantas, kurios naudojamos žaidime.
     */
    public static class EnemyConstants{
        public final static int GOOMBA = 0;

        public final static int RUNNING = 0;
        public final static int DEAD = 1;

        public static final int GOOMBA_WIDTH_DEFAULT = 16;
        public static final int GOOMBA_HEIGHT_DEFAULT = 16;
        public static final int GOOMBA_WIDTH = (int) (GOOMBA_WIDTH_DEFAULT * Game.SCALE);
        public static final int GOOMBA_HEIGHT = (int) (GOOMBA_HEIGHT_DEFAULT * Game.SCALE);

        /**
         * Grąžina priešo animacijos kadrų skaičių pagal priešo tipą ir būseną.
         *
         * @param enemyType   priešo tipas
         * @param enemyState  priešo būsena
         * @return kadrų skaičius
         */
        public static int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case GOOMBA:
                    switch (enemyState) {
                        case RUNNING:
                            return 2;
                        case DEAD:
                            return 1;
                        default:
                            return 0;
                    }
            }
            return 0;
        }
    }

    /**
     * Klasė, sauganti naudotojo sąsajos (UI) mygtukų konstantas.
     */
    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }
    }

    /**
     * Klasė, sauganti judėjimo kryptis konstantas.
     */
    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    /**
     * Klasė, sauganti žaidėjo konstantas.
     */
    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int DEAD = 3;
        public static final int FINISH = 4;

        /**
         * Grąžina žaidėjo animacijos kadrų skaičių pagal veiksmą.
         *
         * @param player_action  žaidėjo veiksmas
         * @return kadrų skaičius
         */
        public static int GetSpriteAmount(int player_action) {
            switch(player_action) {
                case RUNNING:
                    return 3;
                case IDLE:
                case JUMP:
                case DEAD:
                case FINISH:
                default:
                    return 1;
            }
        }
    }

    /**
     * Klasė, sauganti susidūrimo konstantas.
     */
    public static class CollisionConstants {
        public static final int COLLISION_NONE = 0;
        public static final int COLLISION_TOP = 1;
        public static final int COLLISION_BOTTOM = 2;
        public static final int COLLISION_LEFT = 3;
        public static final int COLLISION_RIGHT = 4;
    }

}
