package snakegame;

public interface GameState {
    // Metodi principali per gestire comportamenti specifici dello stato
    void update(Game game);

    void handleInput(Game game, int direction);

    // Transizioni tra stati
    void enterState(Game game);

    void exitState(Game game);

    // Altri metodi utili
    boolean isGameOver();

    boolean isPaused();
}