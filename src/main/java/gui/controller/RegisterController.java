package gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trainer.UserService;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField roleField;
    @FXML private Label statusLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onRegister() {
        boolean success = userService.register(usernameField.getText(), passwordField.getText(), roleField.getText());
        if (success) {
            statusLabel.setText("✅ Registrierung erfolgreich.");
        } else {
            statusLabel.setText("❌ Benutzername existiert bereits.");
        }
    }

    @FXML
    public void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            statusLabel.setText("Fehler beim Zurückkehren.");
        }
    }
}