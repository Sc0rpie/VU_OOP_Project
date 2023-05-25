package gamestates;

/**
 * Žaidimo būsenos enumeratorius
 */
public enum Gamestate {

    /**
     * Žaidimo būsenos.
     */
    PLAYING, MENU, OPTIONS, QUIT;

    /**
     * Žaidimo būsenos kintamasis.
     */
    public static Gamestate state = MENU;
}
