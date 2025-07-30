package gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trainer.User;
import trainer.UserService;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onLogin() {
        User user = userService.login(usernameField.getText(), passwordField.getText());
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/main_menu.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                ((MainMenuController) loader.getController()).setCurrentUser(user);
            } catch (Exception e) {
                statusLabel.setText("Fehler beim Laden des Hauptmenüs.");
            }
        } else {
            statusLabel.setText("❌ Login fehlgeschlagen.");
        }
    }

    @FXML
    public void onRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            statusLabel.setText("Fehler beim Öffnen des Registrierungsfensters.");
        }
    }
}