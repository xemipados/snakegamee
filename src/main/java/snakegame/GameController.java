package snakegame;

import javafx.scene.input.KeyCode;

public class GameController {
    private Game game;
    private SnakeGameGUI view;

    public GameController(SnakeGameGUI view) {
        this.view = view;
    }

    public void initializeGame() {
        Cell initPos = new Cell(0, 0);
        Snake initSnake = new Snake(initPos);
        Board board = new Board(15, 15);
        game = new Game(initSnake, board);
        game.setDirection(Game.DIRECTION_RIGHT);
        board.generateFood();
    }

    public void handleKeyPress(KeyCode key) {
        if (game == null || game.isGameOver())
            return;

        if (key == KeyCode.UP && game.getDirection() != Game.DIRECTION_DOWN) {
            game.setDirection(Game.DIRECTION_UP);
        } else if (key == KeyCode.DOWN && game.getDirection() != Game.DIRECTION_UP) {
            game.setDirection(Game.DIRECTION_DOWN);
        } else if (key == KeyCode.LEFT && game.getDirection() != Game.DIRECTION_RIGHT) {
            game.setDirection(Game.DIRECTION_LEFT);
        } else if (key == KeyCode.RIGHT && game.getDirection() != Game.DIRECTION_LEFT) {
            game.setDirection(Game.DIRECTION_RIGHT);
        }
    }

    public void update() {
        if (game != null && !game.isGameOver()) {
            game.update();
            view.updateView(game);
        }
    }

    public Game getGame() {
        return game;
    }
}