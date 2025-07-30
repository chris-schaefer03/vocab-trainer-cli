package gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import trainer.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class MainMenuController {

    @FXML private Label welcomeLabel;
    @FXML private TextField searchField;
    @FXML private TextField deutschField;
    @FXML private TextField albanischField;
    @FXML private HBox searchBox;
    @FXML private HBox addBox;
    @FXML private ListView<String> vocabListView;

    private User currentUser;
    private final VocabService vocabService = new VocabService();
    private final StatsManager stats = StatsManager.load();
    private final QuizManager quizManager = new QuizManager(vocabService.getAllVocabs(), stats);

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Willkommen, " + user.getUsername() + " (" + user.getRole() + ")");
    }

    @FXML
    public void onAddVocab() {
        if (addBox == null) return;
        addBox.setVisible(true);
        addBox.setManaged(true);
    }

    @FXML
    public void onSaveVocab() {
        String deutsch = deutschField.getText().trim();
        String albanisch = albanischField.getText().trim();

        if (deutsch.isEmpty() || albanisch.isEmpty()) {
            vocabListView.getItems().setAll("❗ Beide Felder ausfüllen!");
            return;
        }

        boolean added = vocabService.addVocab(deutsch, albanisch);
        if (added) {
            vocabListView.getItems().setAll("✅ Vokabel hinzugefügt: " + deutsch + " → " + albanisch);
            deutschField.clear();
            albanischField.clear();
        } else {
            vocabListView.getItems().setAll("⚠️ Vokabel existiert bereits.");
        }
    }

    @FXML
    public void onListVocabs() {
        searchBox.setVisible(false);
        searchBox.setManaged(false);
        vocabListView.getItems().clear();
        vocabService.getAllVocabs().forEach(v -> vocabListView.getItems().add(v.toString()));
    }

    @FXML
    public void onStartQuiz() {
        List<Vocab> dueVocabs = vocabService.getAllVocabs().stream()
                .filter(v -> !LocalDate.now().isBefore(v.getNextReviewDate()))
                .toList();

        if (dueVocabs.isEmpty()) {
            vocabListView.getItems().setAll("🎉 Keine fälligen Vokabeln heute.");
            return;
        }

        boolean deutschZuAlbanisch = askDirection();
        List<String> sessionResults = new ArrayList<>();
        stats.reset();

        for (Vocab v : dueVocabs) {
            String question = deutschZuAlbanisch ? v.getDeutsch() : v.getAlbanisch();
            String expected = deutschZuAlbanisch ? v.getAlbanisch() : v.getDeutsch();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Vokabel-Quiz");
            dialog.setHeaderText(null);
            dialog.setContentText("Was heißt '" + question + "' auf " + (deutschZuAlbanisch ? "Albanisch" : "Deutsch") + "?");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String answer = result.get().trim();
                boolean correct = UIHelper.checkAnswerAndPrint(answer, expected, v, stats, deutschZuAlbanisch, new ArrayList<>());
                sessionResults.add(question + " → " + answer + (correct ? " ✅" : " ❌ (" + expected + ")"));
            }
        }

        vocabListView.getItems().setAll("📊 Ergebnis:");
        vocabListView.getItems().addAll(sessionResults);
        vocabListView.getItems().add(String.format("Trefferquote: %.2f %%", stats.getAccuracyPercent()));
    }


    @FXML
    public void onStartErrorQuiz() {
        List<Vocab> fehlerliste = VocabRepository.loadFehlerliste();

        if (fehlerliste.isEmpty()) {
            vocabListView.getItems().setAll("🎉 Keine Fehler vorhanden – gut gemacht!");
            return;
        }

        boolean deutschZuAlbanisch = askDirection();
        List<String> sessionResults = new ArrayList<>();
        List<Vocab> falschBeantwortet = new ArrayList<>();

        for (Vocab v : fehlerliste) {
            String question = deutschZuAlbanisch ? v.getDeutsch() : v.getAlbanisch();
            String expected = deutschZuAlbanisch ? v.getAlbanisch() : v.getDeutsch();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Fehler-Quiz");
            dialog.setHeaderText(null);
            dialog.setContentText("Was heißt '" + question + "' auf " + (deutschZuAlbanisch ? "Albanisch" : "Deutsch") + "?");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String answer = result.get().trim();
                boolean correct = UIHelper.checkAnswerAndPrint(answer, expected, v, stats, deutschZuAlbanisch, falschBeantwortet);
                sessionResults.add(question + " → " + answer + (correct ? " ✅" : " ❌ (" + expected + ")"));
                if (!correct && !falschBeantwortet.contains(v)) falschBeantwortet.add(v);
            }
        }

        VocabRepository.saveFehlerliste(falschBeantwortet);
        vocabListView.getItems().setAll("📊 Fehler-Quiz Ergebnis:");
        vocabListView.getItems().addAll(sessionResults);
    }

    @FXML
    public void onShowSearchBox() {
        searchBox.setVisible(true);
        searchBox.setManaged(true);
        searchField.clear();
        vocabListView.getItems().setAll("🔍 Bitte Suchbegriff eingeben und auf 'Suchen' klicken.");
    }

    @FXML
    public void onSearchVocabs() {
        String query = searchField.getText().trim().toLowerCase();
        vocabListView.getItems().clear();

        if (query.isEmpty()) {
            vocabListView.getItems().add("❗ Bitte einen Suchbegriff eingeben.");
            return;
        }

        List<Vocab> results = vocabService.search(query);
        if (results.isEmpty()) {
            vocabListView.getItems().add("❌ Keine Vokabeln gefunden.");
        } else {
            results.forEach(v -> vocabListView.getItems().add(v.toString()));
        }
    }

    @FXML
    public void onShowStats() {
        searchBox.setVisible(false);
        searchBox.setManaged(false);
        vocabListView.getItems().setAll(
                "📊 Gesamtstatistiken:",
                "Richtig: " + stats.getTotalCorrect(),
                "Falsch: " + stats.getTotalIncorrect(),
                String.format("Trefferquote: %.2f %%", stats.getAccuracyPercent())
        );
    }

    @FXML
    public void onAdminMenu() {
        if (currentUser != null && currentUser.isAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/admin_menu.fxml"));
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                vocabListView.getItems().setAll("❌ Fehler beim Laden des Admin-Menüs.");
            }
        } else {
            vocabListView.getItems().setAll("🚫 Kein Admin-Zugriff.");
        }
    }

    @FXML
    public void onExit() {
        vocabService.saveVocabs();
        stats.save();
        System.exit(0);
    }

    @FXML
    public void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            vocabListView.getItems().setAll("❌ Fehler beim Logout.");
        }
    }

    private boolean askDirection() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quiz-Richtung wählen");
        alert.setHeaderText("Welche Richtung möchtest du üben?");
        alert.setContentText("Bitte wähle aus:");

        ButtonType button1 = new ButtonType("Deutsch → Albanisch");
        ButtonType button2 = new ButtonType("Albanisch → Deutsch");
        alert.getButtonTypes().setAll(button1, button2);

        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(button1) == button1;
    }
}