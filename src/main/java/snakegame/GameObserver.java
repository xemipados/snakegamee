package snakegame;

public interface GameObserver {
    void onFoodEaten(int foodEaten, int score);

    void onGameOver(int finalScore);

    void onMove();

}
