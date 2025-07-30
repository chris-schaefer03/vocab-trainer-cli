package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/login.fxml"));
            Scene contentScene = new Scene(loader.load(), 600, 400);

            // UI-Hinweis oben
            Label hintLabel = new Label("Drücke ESC, um den Vollbildmodus zu verlassen");
            hintLabel.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-text-fill: white; -fx-padding: 10;");

            StackPane root = new StackPane(contentScene.getRoot(), hintLabel);
            StackPane.setAlignment(hintLabel, javafx.geometry.Pos.TOP_CENTER);

            Scene scene = new Scene(root, 600, 400);

            primaryStage.setTitle("Vokabeltrainer");
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint(""); // Keine JavaFX-Einblendung

            // Entferne eigenen Hinweis nach 3 Sekunden
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> root.getChildren().remove(hintLabel));
            pause.play();

            // Bei ESC -> Fenstergröße festlegen
            primaryStage.fullScreenProperty().addListener((obs, wasFullScreen, isNowFullScreen) -> {
                if (!isNowFullScreen) {
                    primaryStage.setWidth(600);
                    primaryStage.setHeight(400);
                    primaryStage.setResizable(false);
                }
            });

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Fehler beim Starten der Anwendung: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}