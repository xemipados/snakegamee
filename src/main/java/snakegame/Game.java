package snakegame;

import java.util.ArrayList;
import java.util.List;

public class Game implements GameObservable {
    public static final int DIRECTION_NONE = 0, DIRECTION_RIGHT = 1, DIRECTION_LEFT = -1, DIRECTION_UP = 2,
            DIRECTION_DOWN = -2;
    private Snake snake;
    private Board board;
    private int direction;
    private boolean gameOver;
    private long startTime;
    private int foodEaten;
    private static final int WIDTH = 15; // cols
    private static final int HEIGHT = 15; // rows

    private List<GameObserver> observers = new ArrayList<>();

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

    public void update() {
        if (!gameOver && !paused) {
            if (direction != DIRECTION_NONE) {
                Cell nextCell = getNextCell(snake.getHead());

                if (snake.checkCrash(nextCell) || nextCell == null) {
                    setDirection(DIRECTION_NONE);
                    gameOver = true;
                } else {
                    if (nextCell.getCellType() == CellType.FOOD) {
                        snake.grow(nextCell);
                        board.generateFood();
                        foodEaten++;
                    } else {
                        snake.move(nextCell);
                    }
                }
            }
        }
    }

    // mi sposto = nuova cella = nuova posizione
    private Cell getNextCell(Cell currentHead) {
        // Codice esistente invariato
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

    private boolean paused = false;

    // E questi metodi:
    public boolean isPaused() {
        return paused;
    }

    public void togglePause() {
        this.paused = !this.paused;
    }

    public Game(Snake snake, Board board) {
        this.snake = snake;
        this.board = board;
        this.startTime = System.currentTimeMillis(); // Imposta l'inizio della partita
        this.foodEaten = 0; // Inizializza il numero di cibi mangiati
        this.direction = DIRECTION_NONE;
    }

    // Metodi getter e setter esistenti...
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
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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