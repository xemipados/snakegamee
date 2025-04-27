package snakegame;

// Stato Game Over
public class GameOverState implements GameState {
    @Override
    public void update(Game game) {
        // Non fa nulla in stato Game Over
    }

    @Override
    public void handleInput(Game game, int direction) {
        // Ignora gli input di direzione durante il Game Over
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Game Over! Final Score: " + game.getFoodEaten() * 10);
        game.setDirection(Game.DIRECTION_NONE);
        game.notifyGameOver(game.getFoodEaten() * 10);
    }

    @Override
    public void exitState(Game game) {
        // Pulizia prima di uscire dallo stato Game Over
    }

    @Override
    public boolean isGameOver() {
        return true;
    }

    @Override
    public boolean isPaused() {
        return false;
    }
}
