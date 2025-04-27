// 3. Aggiorniamo la classe Game per utilizzare il pattern State
package snakegame;

import java.util.ArrayList;
import java.util.List;

public class Game implements GameObservable {
    public static final int DIRECTION_NONE = 0, DIRECTION_RIGHT = 1, DIRECTION_LEFT = -1, DIRECTION_UP = 2,
            DIRECTION_DOWN = -2;
    private Snake snake;
    private Board board;
    private int direction;
    private long startTime;
    private int foodEaten;
    private static final int WIDTH = 15; // cols
    private static final int HEIGHT = 15; // rows

    // Modifica: aggiungiamo lo stato corrente invece di usare variabili booleane
    private GameState currentState;

    private List<GameObserver> observers = new ArrayList<>();

    public Game(Snake snake, Board board) {
        this.snake = snake;
        this.board = board;
        this.startTime = System.currentTimeMillis();
        this.foodEaten = 0;
        this.direction = DIRECTION_NONE;

        // Inizializziamo con lo stato ReadyState
        this.currentState = new ReadyState();
        this.currentState.enterState(this);
    }

    // Nuovo metodo per cambiare stato
    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exitState(this);
        }
        currentState = newState;
        currentState.enterState(this);
    }

    // Metodo per il toggle della pausa modificato
    public void togglePause() {
        if (currentState.isPaused()) {
            // Se siamo in pausa, torniamo allo stato precedente
            GameState previousState = ((PausedState) currentState).getPreviousState();
            changeState(previousState);
        } else if (!currentState.isGameOver()) {
            // Se non siamo in pausa e non è game over, andiamo in pausa
            changeState(new PausedState(currentState));
        }
    }

    // Aggiorniamo il metodo update per delegare al currentState
    public void update() {
        currentState.update(this);
    }

    // Nuovo metodo per gestire l'input
    public void handleInput(int newDirection) {
        currentState.handleInput(this, newDirection);
    }

    // Metodo per incrementare il cibo mangiato
    public void incrementFoodEaten() {
        this.foodEaten++;
    }

    // Metodo utility che viene usato da PlayingState
    public Cell getNextCell(Cell currentHead) {
        int newRow = currentHead.getRow();
        int newCol = currentHead.getCol();

        switch (direction) {
            case DIRECTION_UP:
                newRow--;
                break;
            case DIRECTION_DOWN:
                newRow++;
                break;
            case DIRECTION_LEFT:
                newCol--;
                break;
            case DIRECTION_RIGHT:
                newCol++;
                break;
        }

        if (newRow < 0 || newRow >= HEIGHT || newCol < 0 || newCol >= WIDTH) {
            return null; // Out of bounds, causing a crash
        }

        return board.getCells()[newRow][newCol];
    }

    // Implementazione GameObservable
    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyFoodEaten(int foodEaten, int score) {
        for (GameObserver observer : observers) {
            observer.onFoodEaten(foodEaten, score);
        }
    }

    @Override
    public void notifyGameOver(int finalScore) {
        for (GameObserver observer : observers) {
            observer.onGameOver(finalScore);
        }
    }

    @Override
    public void notifyMove() {
        for (GameObserver observer : observers) {
            observer.onMove();
        }
    }

    // Metodi getter e setter
    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isGameOver() {
        return currentState.isGameOver();
    }

    public boolean isPaused() {
        return currentState.isPaused();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}