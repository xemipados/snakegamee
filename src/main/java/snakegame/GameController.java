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
        game.setDirection(Game.DIRECTION_RIGHT); // Impostiamo la direzione iniziale
        board.generateFood();
    }

    public void handleKeyPress(KeyCode key) {
        if (game == null)
            return;

        // Gestione dello stato di pausa
        if (key == KeyCode.P || key == KeyCode.SPACE) {
            game.togglePause();
            if (game.isPaused()) {
                view.showPauseOverlay();
            } else {
                view.hidePauseOverlay();
            }
            return;
        }

        // Gestiamo le direzioni tramite l'input della tastiera
        int newDirection = Game.DIRECTION_NONE;

        if (key == KeyCode.UP) {
            newDirection = Game.DIRECTION_UP;
        } else if (key == KeyCode.DOWN) {
            newDirection = Game.DIRECTION_DOWN;
        } else if (key == KeyCode.LEFT) {
            newDirection = Game.DIRECTION_LEFT;
        } else if (key == KeyCode.RIGHT) {
            newDirection = Game.DIRECTION_RIGHT;
        }

        // Deleghiamo al game la gestione dell'input secondo lo stato corrente
        if (newDirection != Game.DIRECTION_NONE) {
            game.handleInput(newDirection);
        }
    }

    public void update() {
        if (game != null) {
            game.update();
            view.updateView(game);
        }
    }

    public Game getGame() {
        return game;
    }
}