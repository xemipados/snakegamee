package snakegame;

// Stato di pausa
public class PausedState implements GameState {
    private GameState previousState;

    public PausedState(GameState previousState) {
        this.previousState = previousState;
    }

    @Override
    public void update(Game game) {
        // Non fa nulla mentre è in pausa
    }

    @Override
    public void handleInput(Game game, int direction) {
        // Ignora gli input di direzione durante la pausa
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Game Paused");
    }

    @Override
    public void exitState(Game game) {
        System.out.println("Resuming Game");
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return true;
    }

    public GameState getPreviousState() {
        return previousState;
    }
}
