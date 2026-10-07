import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class Main extends Application {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 25;

    private static final int INITIAL_PLANTS = 375;
    private static final int INITIAL_HERBIVORES = 40;
    private static final int INITIAL_PREDATORS = 12;

    private static final int CELL_SIZE = 20;

    private Environment environment;

    private GridPane gridPane;
    private Image plantImage;
    private Image herbivoreImage;
    private Image predatorImage;

    private Label turnLabel;
    private Label plantsLabel;
    private Label herbivoresLabel;
    private Label predatorsLabel;
    private Label statusLabel;

    private Button nextTurnButton;
    private Button startButton;
    private Button restartButton;

    private ComboBox<String> speedBox;
    private TextField turnsField;

    private Timeline timeline;

    private int remainingTurns;
    private boolean automaticMode;

    @Override
    public void start(Stage stage) {

        environment = new Environment(WIDTH, HEIGHT);

        initializeEnvironment(environment);

        plantImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/Plant.png"
                        )
                )
        );

        herbivoreImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/Herbivore.png"
                        )
                )
        );

        predatorImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/Predator.png"
                        )
                )
        );

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #12151C;"
        );

        root.setPadding(new Insets(15));

        /*
        root.setStyle(
                "-fx-background-color: #202124;"
        );
         */

        Label titleLabel = new Label("ИСКУССТВЕННАЯ ЖИЗНЬ");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitleLabel = new Label(
                "Моделирование искусственной экосистемы"
        );

        subtitleLabel.setStyle(
                "-fx-text-fill: #8B93A7;" +
                        "-fx-font-size: 12px;"
        );

        VBox titleBox = new VBox(
                3,
                titleLabel,
                subtitleLabel
        );

        HBox header = new HBox(titleBox);

        header.setAlignment(Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(15, 22, 15, 22)
        );

        header.setStyle(
                "-fx-background-color: #1A1E27;" +
                        "-fx-border-color: #292E3A;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        root.setTop(header);


        // Поле симуляции
        gridPane = new GridPane();

        gridPane.setHgap(1);
        gridPane.setVgap(1);
        gridPane.setAlignment(Pos.CENTER);

        createGrid();

        // Заголовок
        Label title = new Label("ИСКУССТВЕННАЯ ЖИЗНЬ");

        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        turnLabel = createInfoLabel();
        plantsLabel = createInfoLabel();
        herbivoresLabel = createInfoLabel();
        predatorsLabel = createInfoLabel();
        statusLabel = createInfoLabel();

        statusLabel.setWrapText(true);

        // Заголовок панели статистики
        Label statisticsTitle = new Label("СТАТИСТИКА");
        statisticsTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );


// ===== Текущий ход =====

        Label turnTitle = new Label("ТЕКУЩИЙ ХОД");
        turnTitle.setStyle(
                "-fx-text-fill: #8B93A7;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;"
        );

        turnLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;"
        );

        VBox turnCard = new VBox(2, turnTitle, turnLabel);
        turnCard.setAlignment(Pos.CENTER);
        turnCard.setPadding(new Insets(12));

        turnCard.setStyle(
                "-fx-background-color: #242933;" +
                        "-fx-background-radius: 10;"
        );


// ===== Растения =====

        ImageView plantIcon = new ImageView(plantImage);

        plantIcon.setFitWidth(45);
        plantIcon.setFitHeight(45);
        plantIcon.setPreserveRatio(true);

        Label plantsTitle = new Label("Растения");

        plantsTitle.setStyle(
                "-fx-text-fill: #9AA3B5;" +
                        "-fx-font-size: 13px;"
        );

        plantsLabel.setStyle(
                "-fx-text-fill: #6FD08C;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        VBox plantsInfo = new VBox(
                2,
                plantsTitle,
                plantsLabel
        );

        HBox plantsContent = new HBox(
                12,
                plantIcon,
                plantsInfo
        );

        plantsContent.setAlignment(Pos.CENTER_LEFT);

        VBox plantsCard = new VBox(plantsContent);

        plantsCard.setPadding(new Insets(12));

        plantsCard.setStyle(
                "-fx-background-color: #242933;" +
                        "-fx-background-radius: 10;"
        );


// ===== Травоядные =====

        ImageView herbivoreIcon = new ImageView(herbivoreImage);

        herbivoreIcon.setFitWidth(45);
        herbivoreIcon.setFitHeight(45);
        herbivoreIcon.setPreserveRatio(true);

        Label herbivoresTitle = new Label("Травоядные");

        herbivoresTitle.setStyle(
                "-fx-text-fill: #9AA3B5;" +
                        "-fx-font-size: 13px;"
        );

        herbivoresLabel.setStyle(
                "-fx-text-fill: #F2C94C;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        VBox herbivoresInfo = new VBox(
                2,
                herbivoresTitle,
                herbivoresLabel
        );

        HBox herbivoresContent = new HBox(
                12,
                herbivoreIcon,
                herbivoresInfo
        );

        herbivoresContent.setAlignment(Pos.CENTER_LEFT);

        VBox herbivoresCard = new VBox(herbivoresContent);

        herbivoresCard.setPadding(new Insets(12));

        herbivoresCard.setStyle(
                "-fx-background-color: #242933;" +
                        "-fx-background-radius: 10;"
        );


// ===== Хищники =====

        ImageView predatorIcon = new ImageView(predatorImage);

        predatorIcon.setFitWidth(45);
        predatorIcon.setFitHeight(45);
        predatorIcon.setPreserveRatio(true);

        Label predatorsTitle = new Label("Хищники");

        predatorsTitle.setStyle(
                "-fx-text-fill: #9AA3B5;" +
                        "-fx-font-size: 13px;"
        );

        predatorsLabel.setStyle(
                "-fx-text-fill: #EB6A6A;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        VBox predatorsInfo = new VBox(
                2,
                predatorsTitle,
                predatorsLabel
        );

        HBox predatorsContent = new HBox(
                12,
                predatorIcon,
                predatorsInfo
        );

        predatorsContent.setAlignment(Pos.CENTER_LEFT);

        VBox predatorsCard = new VBox(predatorsContent);

        predatorsCard.setPadding(new Insets(12));

        predatorsCard.setStyle(
                "-fx-background-color: #242933;" +
                        "-fx-background-radius: 10;"
        );


// ===== Статус =====

        statusLabel.setStyle(
                "-fx-text-fill: #8B93A7;" +
                        "-fx-font-size: 12px;"
        );


// ===== Общая панель =====

        VBox statistics = new VBox(
                12,
                statisticsTitle,
                turnCard,
                plantsCard,
                herbivoresCard,
                predatorsCard,
                statusLabel
        );

        statistics.setPrefWidth(280);

        statistics.setPadding(
                new Insets(18)
        );

        statistics.setStyle(
                "-fx-background-color: #1A1E27;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #292E3A;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );



        statistics.setPadding(
                new Insets(15)
        );

        statistics.setStyle(
                "-fx-background-color: #292a2d;" +
                        "-fx-background-radius: 10;"
        );

        nextTurnButton =
                new Button("Следующий ход");

        nextTurnButton.setPrefWidth(150);

        nextTurnButton.setOnAction(
                event -> nextTurn()
        );


        startButton =
                new Button("Запустить");

        startButton.setPrefWidth(150);

        startButton.setOnAction(
                event -> toggleSimulation()
        );


        restartButton =
                new Button("Перезапустить");

        restartButton.setPrefWidth(150);

        restartButton.setOnAction(
                event -> restartSimulation()
        );

        speedBox = new ComboBox<>();

        speedBox.getItems().addAll(
                "Очень медленно (1000 мс)",
                "Медленно (500 мс)",
                "Нормально (250 мс)",
                "Быстро (100 мс)",
                "Очень быстро (50 мс)"
        );

        speedBox.setValue(
                "Очень быстро (50 мс)"
        );

        speedBox.setPrefWidth(200);


        turnsField =
                new TextField("100");

        turnsField.setPrefWidth(80);


        Label speedLabel =
                new Label("Скорость:");

        speedLabel.setStyle(
                "-fx-text-fill: white;"
        );


        Label turnsLabel =
                new Label("Ходов:");

        turnsLabel.setStyle(
                "-fx-text-fill: white;"
        );


        HBox controls =
                new HBox(
                        10,
                        nextTurnButton,
                        startButton,
                        restartButton,
                        speedLabel,
                        speedBox,
                        turnsLabel,
                        turnsField
                );

        controls.setAlignment(Pos.CENTER);

        controls.setPadding(
                new Insets(15, 0, 0, 0)
        );

        HBox legend = new HBox(20);

        legend.setAlignment(Pos.CENTER);

        legend.getChildren().addAll(
                createLegendItem(
                        "Р",
                        "Растение",
                        "#4CAF50"
                ),

                createLegendItem(
                        "Т",
                        "Травоядное",
                        "#FFC107"
                ),

                createLegendItem(
                        "Х",
                        "Хищник",
                        "#F44336"
                ),

                createLegendItem(
                        ".",
                        "Пустая клетка",
                        "#777777"
                )
        );

        VBox bottom =
                new VBox(
                        10,
                        legend,
                        controls
                );

        bottom.setAlignment(Pos.CENTER);


        VBox center =
                new VBox(
                        15,
                        title,
                        gridPane,
                        bottom
                );

        center.setAlignment(Pos.CENTER);


        // Контейнер игрового поля
        VBox gameCard = new VBox(10);

        gameCard.setPadding(
                new Insets(15)
        );

        gameCard.setAlignment(
                Pos.CENTER
        );

        gameCard.setStyle(
                "-fx-background-color: #1A1E27;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #292E3A;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );


// Заголовок над полем
        Label fieldTitle = new Label(
                "Игровое поле"
        );

        fieldTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );


// Само поле
        gridPane.setStyle(
                "-fx-background-color: #252A34;" +
                        "-fx-border-color: #343A46;" +
                        "-fx-border-width: 1;"
        );


        gameCard.getChildren().addAll(
                fieldTitle,
                gridPane
        );


// Отступ карточки от краёв окна
        BorderPane.setMargin(
                gameCard,
                new Insets(18)
        );


        VBox centerContent = new VBox(15);

        centerContent.setAlignment(Pos.CENTER);

        centerContent.setPadding(
                new Insets(18)
        );

        centerContent.getChildren().addAll(
                gameCard,
                controls
        );

        root.setCenter(centerContent);
        root.setRight(statistics);

        BorderPane.setMargin(
                statistics,
                new Insets(0, 0, 0, 15)
        );


        updateView();


        Scene scene =
                new Scene(
                        root,
                        1250,
                        800
                );

        stage.setTitle(
                "Искусственная жизнь"
        );

        stage.setScene(scene);

        stage.setResizable(false);

        stage.show();
    }

    private void createGrid() {

        gridPane.getChildren().clear();

        for (int y = 0; y < HEIGHT; y++) {

            for (int x = 0; x < WIDTH; x++) {

                Rectangle background =
                        new Rectangle(
                                CELL_SIZE,
                                CELL_SIZE
                        );

                background.setFill(
                        Color.web("#242933")
                );

                background.setStroke(
                        Color.web("#343A46")
                );

                ImageView imageView =
                        new ImageView();

                imageView.setFitWidth(
                        CELL_SIZE - 4
                );

                imageView.setFitHeight(
                        CELL_SIZE - 4
                );

                imageView.setPreserveRatio(true);

                StackPane cell =
                        new StackPane(
                                background,
                                imageView
                        );

                gridPane.add(
                        cell,
                        x,
                        y
                );
            }
        }
    }

    private void updateView() {

        for (int y = 0; y < HEIGHT; y++) {

            for (int x = 0; x < WIDTH; x++) {

                StackPane cell =
                        getCell(x, y);

                ImageView imageView =
                        (ImageView) cell
                                .getChildren()
                                .get(1);

                Agent agent =
                        environment.getAgent(x, y);

                // Пустая клетка
                if (agent == null) {

                    imageView.setImage(null);

                }

                // Растение
                else if (agent instanceof Plant) {

                    imageView.setImage(
                            plantImage
                    );

                }

                // Травоядное
                else if (agent instanceof Herbivore) {

                    imageView.setImage(
                            herbivoreImage
                    );

                }

                // Хищник
                else if (agent instanceof Predator) {

                    imageView.setImage(
                            predatorImage
                    );
                }
            }
        }

        updateStatistics();
    }


    private StackPane getCell(
            int x,
            int y
    ) {

        return (StackPane) gridPane
                .getChildren()
                .get(y * WIDTH + x);
    }

    private void nextTurn() {

        if (!environment.hasAllAgentTypes()) {

            stopSimulation();

            statusLabel.setText(
                    "Симуляция завершена:\n" +
                            "один из видов агентов исчез."
            );

            return;
        }

        environment.nextTurn();

        updateView();

        if (!environment.hasAllAgentTypes()) {

            stopSimulation();

            statusLabel.setText(
                    "Симуляция завершена:\n" +
                            "один из видов агентов исчез."
            );

            return;
        }

        if (automaticMode) {

            remainingTurns--;

            if (remainingTurns <= 0) {

                stopSimulation();

                statusLabel.setText(
                        "Автоматическая симуляция завершена."
                );
            }
        }
    }
    private void toggleSimulation() {

        if (automaticMode) {

            stopSimulation();

            statusLabel.setText(
                    "Симуляция приостановлена."
            );

            return;
        }


        int turns;

        try {

            turns =
                    Integer.parseInt(
                            turnsField.getText().trim()
                    );

        } catch (NumberFormatException e) {

            statusLabel.setText(
                    "Ошибка: количество ходов должно " +
                            "быть целым числом."
            );

            return;
        }


        if (turns <= 0) {

            statusLabel.setText(
                    "Количество ходов должно быть больше 0."
            );

            return;
        }


        remainingTurns = turns;

        automaticMode = true;

        startButton.setText("Пауза");

        nextTurnButton.setDisable(true);

        speedBox.setDisable(true);

        turnsField.setDisable(true);


        int delay = getSelectedDelay();


        timeline =
                new Timeline(
                        new KeyFrame(
                                Duration.millis(delay),
                                event -> nextTurn()
                        )
                );

        timeline.setCycleCount(
                Timeline.INDEFINITE
        );


        statusLabel.setText(
                "Автоматическая симуляция запущена."
        );

        timeline.play();
    }

    private void stopSimulation() {

        automaticMode = false;

        if (timeline != null) {

            timeline.stop();
            timeline = null;
        }

        startButton.setText(
                "Запустить"
        );

        nextTurnButton.setDisable(false);
        speedBox.setDisable(false);
        turnsField.setDisable(false);
    }


    private int getSelectedDelay() {

        String selected =
                speedBox.getValue();

        if (selected.startsWith(
                "Очень медленно"
        )) {
            return 1000;
        }

        if (selected.startsWith(
                "Медленно"
        )) {
            return 500;
        }

        if (selected.startsWith(
                "Нормально"
        )) {
            return 250;
        }

        if (selected.startsWith(
                "Быстро"
        )) {
            return 100;
        }

        return 50;
    }

    private void restartSimulation() {

        stopSimulation();

        environment =
                new Environment(
                        WIDTH,
                        HEIGHT
                );

        initializeEnvironment(
                environment
        );

        remainingTurns = 0;

        updateView();

        statusLabel.setText(
                "Симуляция перезапущена."
        );
    }

    private void updateStatistics() {

        int plants = 0;
        int herbivores = 0;
        int predators = 0;

        for (Agent agent :
                environment.getAgents()) {

            if (agent instanceof Plant) {

                plants++;

            } else if (agent instanceof Herbivore) {

                herbivores++;

            } else if (agent instanceof Predator) {

                predators++;
            }
        }


        turnLabel.setText(
                "Ход: " +
                        environment.getTurn()
        );

        plantsLabel.setText(
                ": " +
                        plants
        );

        herbivoresLabel.setText(
                ": " +
                        herbivores
        );

        predatorsLabel.setText(
                ": " +
                        predators
        );
    }

    private void initializeEnvironment(
            Environment environment
    ) {

        createPlants(
                environment,
                INITIAL_PLANTS
        );

        createHerbivores(
                environment,
                INITIAL_HERBIVORES
        );

        createPredators(
                environment,
                INITIAL_PREDATORS
        );
    }

    private void createPlants(
            Environment environment,
            int count
    ) {

        int created = 0;

        while (created < count) {

            int x =
                    Environment.RANDOM.nextInt(
                            environment.getWidth()
                    );

            int y =
                    Environment.RANDOM.nextInt(
                            environment.getHeight()
                    );

            if (environment.getAgent(x, y) == null) {

                environment.addAgent(
                        new Plant(x, y)
                );

                created++;
            }
        }
    }


    private void createHerbivores(
            Environment environment,
            int count
    ) {

        int created = 0;

        while (created < count) {

            int x =
                    Environment.RANDOM.nextInt(
                            environment.getWidth()
                    );

            int y =
                    Environment.RANDOM.nextInt(
                            environment.getHeight()
                    );

            if (environment.getAgent(x, y) == null) {

                environment.addAgent(
                        new Herbivore(x, y)
                );

                created++;
            }
        }
    }


    private void createPredators(
            Environment environment,
            int count
    ) {

        int created = 0;

        while (created < count) {

            int x =
                    Environment.RANDOM.nextInt(
                            environment.getWidth()
                    );

            int y =
                    Environment.RANDOM.nextInt(
                            environment.getHeight()
                    );

            if (environment.getAgent(x, y) == null) {

                environment.addAgent(
                        new Predator(x, y)
                );

                created++;
            }
        }
    }

    private Label createInfoLabel() {

        Label label = new Label();

        label.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;"
        );

        return label;
    }


    private Label createSectionTitle(
            String text
    ) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        return label;
    }


    private HBox createLegendItem(
            String symbol,
            String text,
            String color
    ) {

        Label symbolLabel =
                new Label(symbol);

        symbolLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " +
                        color + ";"
        );


        Label textLabel =
                new Label(text);

        textLabel.setStyle(
                "-fx-text-fill: white;"
        );


        HBox box =
                new HBox(
                        5,
                        symbolLabel,
                        textLabel
                );

        box.setAlignment(Pos.CENTER);

        return box;
    }

    @Override
    public void stop() {

        if (timeline != null) {
            timeline.stop();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}