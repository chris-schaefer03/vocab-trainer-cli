package gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import trainer.StatsManager;
import trainer.User;
import trainer.UserService;

import java.io.IOException;
import java.util.Optional;

public class AdminMenuController {
    @FXML private Label outputLabel;
    private final StatsManager stats = StatsManager.load();
    private final UserService userService = new UserService();

    @FXML
    public void onResetStats() {
        stats.reset();
        stats.save();
        outputLabel.setText("🧹 Statistiken wurden zurückgesetzt.");
    }

    @FXML
    public void onShowUsers() {
        StringBuilder sb = new StringBuilder("👥 Benutzer:\n");
        userService.getAllUsers().forEach(u -> sb.append(u.getUsername()).append(" (" + u.getRole() + ")\n"));
        outputLabel.setText(sb.toString());
    }

    @FXML
    public void onDeleteUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Benutzer löschen");
        dialog.setHeaderText(null);
        dialog.setContentText("Benutzername zum Löschen:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            boolean success = userService.deleteUser(username, "admin");
            outputLabel.setText(success ? "🗑️ Benutzer gelöscht." : "❌ Benutzer konnte nicht gelöscht werden.");
        });
    }

    @FXML
    public void onRegisterUser() {
        outputLabel.setText("[GUI] Benutzer registrieren ausgelöst (noch nicht implementiert)");
    }

    @FXML
    public void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/main_menu.fxml"));
            Stage stage = (Stage) outputLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            outputLabel.setText("Fehler beim Zurückkehren zum Hauptmenü.");
        }
    }
}