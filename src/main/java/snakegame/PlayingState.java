package snakegame;

// Stato di gioco principale
public class PlayingState implements GameState {
    @Override
    public void update(Game game) {
        if (game.getDirection() != Game.DIRECTION_NONE) {
            Cell nextCell = game.getNextCell(game.getSnake().getHead());

            if (nextCell == null || game.getSnake().checkCrash(nextCell)) {
                game.changeState(new GameOverState());
                return;
            }

            if (nextCell.getCellType() == CellType.FOOD) {
                game.getSnake().grow(nextCell);
                game.getBoard().generateFood();
                game.incrementFoodEaten();
                game.notifyFoodEaten(game.getFoodEaten(), game.getFoodEaten() * 10);
            } else {
                game.getSnake().move(nextCell);
            }

            game.notifyMove();
        }
    }

    @Override
    public void handleInput(Game game, int direction) {
        // Verifica che la nuova direzione non sia opposta alla direzione corrente
        int currentDirection = game.getDirection();

        if ((direction == Game.DIRECTION_UP && currentDirection != Game.DIRECTION_DOWN) ||
                (direction == Game.DIRECTION_DOWN && currentDirection != Game.DIRECTION_UP) ||
                (direction == Game.DIRECTION_LEFT && currentDirection != Game.DIRECTION_RIGHT) ||
                (direction == Game.DIRECTION_RIGHT && currentDirection != Game.DIRECTION_LEFT)) {
            game.setDirection(direction);
        }
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Game Started - Playing");
    }

    @Override
    public void exitState(Game game) {
        // Preparazioni prima di uscire dallo stato Playing
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