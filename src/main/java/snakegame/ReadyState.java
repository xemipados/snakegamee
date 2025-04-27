package snakegame;

// Stato iniziale: Ready (prima dell'inizio della partita)
public class ReadyState implements GameState {
    @Override
    public void update(Game game) {
        // Non fa nulla mentre è in stato Ready
    }

    @Override
    public void handleInput(Game game, int direction) {
        if (direction != Game.DIRECTION_NONE) {
            game.setDirection(direction);
            game.changeState(new PlayingState());
        }
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Game Ready - Press arrow keys to start");
    }

    @Override
    public void exitState(Game game) {
        // Preparazioni prima di uscire dallo stato Ready
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }
}
