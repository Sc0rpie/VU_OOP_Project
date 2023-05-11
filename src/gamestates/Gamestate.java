package gamestates;

public enum Gamestate {

    PLAYING, MENU, OPTIONS, QUIT, IO, SAVE, LOAD; // IO - Saving and loading menu

    public static Gamestate state = IO;
}
