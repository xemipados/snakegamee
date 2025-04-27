package snakegame;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeGameGUI extends Application implements GameObserver {

    private static final int TILE_SIZE = 40; // Dimensione di ogni cella
    private static final int WIDTH = 15; // Numero di colonne
    private static final int HEIGHT = 15; // Numero di righe
    private GameController controller;

    private Canvas canvas;
    private Stage primaryStage;
    protected Thread threadPanel;
    protected Image mushroom;
    protected Image snakeSegment;
    protected Image snakeImage;
    protected Image snakeUp;
    protected Image snakeDown;
    protected Image snakeLeft;
    protected Image snakeRight;
    protected Image snakeHeadDead;

    @Override
    public void start(Stage thePrimaryStage) {
        primaryStage = thePrimaryStage;
        primaryStage.setTitle("Snake Game");

        // primaryStage.initStyle(StageStyle.UNDECORATED); // Rimuove i bordi della
        // finestra (mhhh valuta se è carino o meno :/ )

        mushroom = new Image("/fungoGIUSTO.png");
        snakeSegment = new Image("/snakebodyGIUSTOOOOO.png");
        snakeUp = new Image("/snakeSUGIUSTOpd.png");
        snakeDown = new Image("/snakeGIUGIUSTOpd.png");
        snakeLeft = new Image("/snakeSXGIUSTOpd.png");
        snakeRight = new Image("/snakeDXGIUSTOpd.png");
        snakeHeadDead = new Image("/DeadSnakeGIUSTOpd.png");

        Image cursorImage = new Image(getClass().getResourceAsStream("/cursor.png"));
        Cursor customCursor = new ImageCursor(cursorImage);

        // Imposta l'icona del programma
        Image icon = new Image(getClass().getResourceAsStream("/SnWoutBckGIUSTO.png"));
        primaryStage.getIcons().add(icon);

        controller = new GameController(this);

        int borderThickness = 10; // Spessore del bordo
        int offset = 10; // Offset per il bordo
        canvas = new Canvas(WIDTH * TILE_SIZE + 2 * borderThickness,
                HEIGHT * TILE_SIZE + TILE_SIZE + 2 * borderThickness + offset);

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        showGameIconWithFade(root);
        scene.setCursor(customCursor);

        // Ascolto degli input della tastiera
        scene.setOnKeyPressed(event -> {
            controller.handleKeyPress(event.getCode());
        });

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0)); // Chiude il programma quando viene chiusa la finestra
        primaryStage.show();

        // Mostra il conto alla rovescia prima di iniziare il gioco
        showCountdown();
    }

    public void updateView(Game game) {
        Platform.runLater(() -> {
            showPoints(game);
            drawGame(game);
        });
    }

    private void showPoints(Game game) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Colori vari
        Color scoreTextColor = Color.web("#445414");
        Color scoreBoxColor = Color.web("#d0d058", 0.9);
        Color borderColor = Color.web("#425110");

        // Dimensioni del riquadro del punteggio
        double scoreBoxWidth = 250;
        double scoreBoxHeight = 40;
        double scoreBoxX = 180;
        double scoreBoxY = 10;
        double arcWidth = 20;
        double arcHeight = 20;

        // libero spazio per la barra del punteggio e disegno il bordo
        gc.setFill(borderColor);
        gc.fillRect(0, 0, WIDTH * TILE_SIZE + 80, TILE_SIZE + 50); // Colore del bordo superiore

        // Disegna il riquadro stondato attorno al punteggio
        gc.setFill(scoreBoxColor);
        gc.fillRoundRect(scoreBoxX, scoreBoxY, scoreBoxWidth, scoreBoxHeight, arcWidth, arcHeight);

        // Disegna l'immagine del fungo accanto al punteggio
        double mushroomSize = 30;
        double mushroomX = scoreBoxX + 10;
        double mushroomY = scoreBoxY + (scoreBoxHeight - mushroomSize) / 2;
        gc.drawImage(mushroom, mushroomX, mushroomY, mushroomSize, mushroomSize);

        // Disegna il testo del punteggio accanto all'immagine del fungo
        gc.setFill(scoreTextColor);
        gc.setFont(javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 35));
        double textX = mushroomX + mushroomSize + 10;
        double textY = scoreBoxY + (scoreBoxHeight + 20) / 2;
        gc.fillText("Score:" + (game.getFoodEaten() * 10), textX, textY);
    }

    private void drawGameBorder(GraphicsContext gc) {
        int borderThickness = 10; // Spessore del bordo
        int arcSize = 30; // Raggio di arrotondamento degli angoli
        int width = WIDTH * TILE_SIZE;
        int height = HEIGHT * TILE_SIZE;

        // Colore per il bordo esterno
        gc.setFill(Color.web("#435211"));
        gc.fillRoundRect(0, TILE_SIZE + 10, width + 2 * borderThickness, height + 2 * borderThickness + 100, arcSize,
                arcSize);

        // Riempie l'interno dell'area di gioco con un colore di sfondo
        gc.setFill(Color.web("#a1a940"));
        gc.fillRoundRect(borderThickness, TILE_SIZE + 10 + borderThickness, width, height, arcSize, arcSize);

    }

    private void showCountdown() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Inizializza il canvas con un colore di sfondo
        gc.setFill(Color.web("#71802b"));
        gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 60);

        Platform.runLater(() -> {
            System.out.println("Countdown inizia ora...");

            // Disegna il conto alla rovescia
            Platform.runLater(() -> {
                gc.setFill(Color.rgb(112, 128, 40));
                gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);
                gc.setFill(Color.rgb(208, 208, 88));
                gc.setFont(javafx.scene.text.Font.font(48));
                gc.fillText(" ", WIDTH * TILE_SIZE / 2 - 10, HEIGHT * TILE_SIZE / 2);
            });

            PauseTransition pause4 = new PauseTransition(Duration.seconds(1));
            pause4.setOnFinished(event6 -> {
                Platform.runLater(() -> {
                    gc.setFill(Color.rgb(112, 128, 40));
                    gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);
                    gc.setFill(Color.rgb(208, 208, 88));
                    gc.setFont(javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 80));
                    gc.fillText("3", WIDTH * TILE_SIZE / 2 - 10, HEIGHT * TILE_SIZE / 2 + 30);
                });
                PauseTransition pause3 = new PauseTransition(Duration.seconds(1));
                pause3.setOnFinished(event -> {
                    Platform.runLater(() -> {
                        gc.setFill(Color.rgb(112, 128, 40));
                        gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);
                        gc.setFill(Color.rgb(208, 208, 88));
                        gc.setFont(javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 80));
                        gc.fillText("2", WIDTH * TILE_SIZE / 2 - 10, HEIGHT * TILE_SIZE / 2 + 30);
                    });
                    PauseTransition pause2 = new PauseTransition(Duration.seconds(1));
                    pause2.setOnFinished(event2 -> {
                        Platform.runLater(() -> {
                            gc.setFill(Color.rgb(112, 128, 40));
                            gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);
                            gc.setFill(Color.rgb(208, 208, 88));
                            gc.setFont(
                                    javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 80));
                            gc.fillText("1", WIDTH * TILE_SIZE / 2 - 10, HEIGHT * TILE_SIZE / 2 + 30);
                        });
                        PauseTransition pause1 = new PauseTransition(Duration.seconds(1));
                        pause1.setOnFinished(event3 -> {
                            Platform.runLater(() -> {
                                gc.setFill(Color.rgb(112, 128, 40));
                                gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);
                                gc.setFill(Color.rgb(208, 208, 88));
                                gc.setFont(javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD,
                                        80));
                                gc.fillText("GO!", WIDTH * TILE_SIZE / 2 - 50, HEIGHT * TILE_SIZE / 2 + 30);
                            });
                            PauseTransition pauseGo = new PauseTransition(Duration.seconds(1));
                            pauseGo.setOnFinished(event4 -> {
                                Platform.runLater(() -> {
                                    gc.setFill(Color.rgb(112, 128, 40));
                                    gc.fillRect(0, 0, WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);
                                    gc.setFill(Color.rgb(160, 168, 64));
                                });
                                initializeGame();
                                startGameLoop();
                            });
                            pauseGo.play();
                        });
                        pause1.play();
                    });
                    pause2.play();
                });
                pause3.play();
            });

            pause4.play();
        });

    }

    // Loop di aggiornamento
    private void startGameLoop() {
        new Thread(() -> {
            double duration = 1_000_000_000 / 60; // Configurations.FPS;
            double delta = 0;
            long previousTime = System.nanoTime();
            long now;
            long drawUpdate = 10;
            long update = 0;

            Game game = controller.getGame(); // Ottieni il riferimento al gioco dal controller

            while (!Thread.interrupted()) { // Cambiato da !game.isGameOver()
                now = System.nanoTime();
                delta += (now - previousTime) / duration;
                previousTime = now;
                if (delta >= 1) {
                    if (update == 0) {
                        controller.update(); // Usa il controller per aggiornare il gioco
                    }
                    update += 1;
                    update = update % drawUpdate;
                    delta--;
                }

                // Se il gioco è terminato, esci dal ciclo
                if (game.isGameOver()) {
                    break;
                }
            }
        }).start();
    }

    private void initializeGame() {
        controller.initializeGame();
        controller.getGame().addObserver(this);
    }

    private void drawGame(Game game) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // bordo
        drawGameBorder(gc);

        // griglia di gioco
        Cell[][] cells = game.getBoard().getCells();
        int direzione = Game.DIRECTION_NONE;
        double arcWidth = 20; // Arrotondamento degli angoli
        double arcHeight = 20; // Arrotondamento degli angoli

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Cell cell = cells[row][col];
                if (cell.getCellType() == CellType.SNAKE_NODE) {
                    // Verifica se è la testa del serpente
                    if (game.getSnake().isHead(cell)) {
                        direzione = game.getDirection();
                        if (game.isGameOver()) {
                            gc.drawImage(snakeHeadDead, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                        } else {
                            switch (direzione) {
                                case Game.DIRECTION_DOWN:
                                    gc.drawImage(snakeDown, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                                    break;
                                case Game.DIRECTION_UP:
                                    gc.drawImage(snakeUp, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                                    break;
                                case Game.DIRECTION_LEFT:
                                    gc.drawImage(snakeLeft, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                                    break;
                                case Game.DIRECTION_RIGHT:
                                    gc.drawImage(snakeRight, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                                    break;
                            }
                        }
                    } else {
                        // Disegna il corpo del serpente normalmente
                        gc.drawImage(snakeSegment, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                    }
                } else if (cell.getCellType() == CellType.FOOD) {
                    // Disegna il cibo
                    gc.drawImage(mushroom, col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20);
                } else {
                    // Disegna lo sfondo
                    gc.setFill(Color.web("#a1a940"));
                    gc.fillRoundRect(col * TILE_SIZE + 10, row * TILE_SIZE + TILE_SIZE + 20, TILE_SIZE - 10,
                            TILE_SIZE - 10,
                            arcWidth, arcHeight);
                }
            }
        }
    }

    // FINITO ALERT!!!!!
    private void showGameOver() {
        // Calcola il punteggio ottenuto
        int score = controller.getGame().getFoodEaten() * 10; // Il punteggio è 10 punti per ogni cibo mangiato
        Image cursorImage = new Image(getClass().getResourceAsStream("/cursor.png"));
        Cursor customCursor = new ImageCursor(cursorImage);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Game Over");
        dialog.setResizable(false);
        // Aggiungi testo e pulsanti
        Label message = new Label("Game over!\nScore: " + score);

        message.setStyle(
                "-fx-font-family: 'Monospaced'; -fx-font-size: 30px; -fx-text-fill: #708028; -fx-background-color: #d0d058; -fx-padding: 10px;-fx-background-radius: 5px; -fx-font-weight: bolder;");

        Button yesButton = new Button("Retry");
        yesButton.setStyle(
                "-fx-font-family: 'Monospaced'; -fx-background-color: #bbbb4f; -fx-text-fill: #405010; -fx-padding: 15px; -fx-font-size: 18px; -fx-background-radius: 5px; -fx-font-weight: bold;");
        yesButton.setOnAction(e -> {
            showCountdown(); // Mostra il conto alla rovescia prima di riavviare
            dialog.close();
        });

        Button noButton = new Button("Exit");
        noButton.setStyle(
                "-fx-font-family: 'Monospaced'; -fx-background-color: rgba(84, 10, 20, 0.65); -fx-text-fill: rgba( 208, 208, 88, 0.95); -fx-padding: 15px; -fx-font-size: 18px; -fx-background-radius: 5px;-fx-font-weight: bold; ");
        noButton.setOnAction(e -> {
            primaryStage.close(); // Chiudi la finestra se l'utente seleziona "Exit"
            dialog.close();
        });

        HBox buttons = new HBox(10, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, message, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #71802b;");

        Scene scene = new Scene(layout, 400, 300);

        scene.setCursor(customCursor);

        dialog.setScene(scene);
        dialog.showAndWait();

    }

    public static void main(String[] args) {
        launch();
    }

    private void showGameIconWithFade(Pane root) {
        ImageView gameIcon = new ImageView(new Image(getClass().getResourceAsStream("/SnWoutBckGIUSTO.png")));

        // Imposta dimensioni per l'icona
        gameIcon.setFitHeight(320); // Altezza dell'icona
        gameIcon.setFitWidth(320); // Larghezza dell'icona

        // Centra l'icona rispetto alla finestra di gioco
        gameIcon.setLayoutX((WIDTH * TILE_SIZE + 30) / 2 - gameIcon.getFitWidth() / 2);
        gameIcon.setLayoutY((HEIGHT * TILE_SIZE + 20) / 2 - gameIcon.getFitHeight() / 2);

        root.getChildren().add(gameIcon);

        // Iniziamo senza sfumature
        gameIcon.setOpacity(1);

        // Transizione solo per il fade-out (in uscita)
        PauseTransition pause = new PauseTransition(Duration.seconds(0.8)); // Durata della pausa
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), gameIcon); // Fade-out di 1 secondo
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> root.getChildren().remove(gameIcon));
            fadeOut.play();
        });

        pause.play(); // Avvia la pausa per poi procedere con il fade-out
    }

    @Override
    public void onFoodEaten(int foodEaten, int score) {
        // Qui puoi aggiungere effetti o animazioni quando viene mangiato il cibo
        System.out.println("Food eaten! Score: " + score);

        // Puoi aggiungere un effetto visivo temporaneo, come un'animazione veloce
        // o un breve flash del punteggio
    }

    @Override
    public void onGameOver(int finalScore) {
        // Usa Platform.runLater perché questo metodo potrebbe essere
        // chiamato da un thread diverso da quello JavaFX
        Platform.runLater(() -> {
            System.out.println("Game Over! Final Score: " + finalScore);
            showGameOver();
        });
    }

    // Questo metodo viene chiamato ad ogni movimento del serpente
    @Override
    public void onMove() {

        System.out.println("Snake moved");
    }

    private Pane pauseOverlay;

    public void showPauseOverlay() {
        if (pauseOverlay == null) {
            createPauseOverlay();
        }

        Platform.runLater(() -> {
            Pane root = (Pane) primaryStage.getScene().getRoot();
            if (!root.getChildren().contains(pauseOverlay)) {
                root.getChildren().add(pauseOverlay);
            }
            pauseOverlay.setVisible(true);
        });
    }

    public void hidePauseOverlay() {
        if (pauseOverlay != null) {
            Platform.runLater(() -> {
                pauseOverlay.setVisible(false);
            });
        }
    }

    private void createPauseOverlay() {
        pauseOverlay = new Pane();
        pauseOverlay.setPrefSize(WIDTH * TILE_SIZE + 20, HEIGHT * TILE_SIZE + TILE_SIZE + 20);

        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(
                WIDTH * TILE_SIZE + 20,
                HEIGHT * TILE_SIZE + TILE_SIZE + 20);
        rect.setFill(Color.rgb(68, 84, 20, 0.7)); // Colore con trasparenza

        Label pauseLabel = new Label("PAUSA");
        pauseLabel.setFont(javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 60));
        pauseLabel.setTextFill(Color.rgb(208, 208, 88, 0.9));

        // Testo istruzioni
        Label instructionLabel = new Label("Premi P o SPAZIO per continuare");
        instructionLabel.setFont(javafx.scene.text.Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 20));
        instructionLabel.setTextFill(Color.rgb(208, 208, 88, 0.9));

        // Contenitore per centrare le etichette
        VBox vbox = new VBox(20, pauseLabel, instructionLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX((WIDTH * TILE_SIZE + 20) / 2 - 180);
        vbox.setLayoutY((HEIGHT * TILE_SIZE + TILE_SIZE + 20) / 2 - 70);

        pauseOverlay.getChildren().addAll(rect, vbox);
        pauseOverlay.setVisible(false);
    }

}
