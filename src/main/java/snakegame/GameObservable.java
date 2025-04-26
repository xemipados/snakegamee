package snakegame;

public interface GameObservable {
    void addObserver(GameObserver observer);

    void removeObserver(GameObserver observer);

    void notifyFoodEaten(int foodEaten, int score);

    void notifyGameOver(int finalScore);

    void notifyMove();
}
