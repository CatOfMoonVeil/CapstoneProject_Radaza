package com.example.ridehailing.ui;

import com.example.ridehailing.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class DriverSignUpController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField vehicleField;
    @FXML private PasswordField passwordField;
    @FXML private Button signUpButton;
    @FXML private Label statusLabel;

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @FXML
    private void handleSignUp() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String vehicle = vehicleField.getText().trim();
        String password = passwordField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || vehicle.isEmpty() || password.isEmpty()) {
            showError("All driver registration fields are required!");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email format.");
            return;
        }

        try {
            UserSession.registerDriver(name, phone, email, password, vehicle);

            statusLabel.setStyle("-fx-text-fill: #10ac84; -fx-font-weight: bold;");
            statusLabel.setText("Driver Profile Created! Redirecting...");
            handleBackToLogin();
        } catch (SQLException e) {
            if (e.getMessage().contains("already registered")) {
                showError("Sign Up failed: Email is already associated with an account.");
            } else {
                showError("System error connecting with database.");
                e.printStackTrace();
            }
        }
    }

    private boolean isValidEmail(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }

    private void showError(String msg) {
        statusLabel.setStyle("-fx-text-fill: #ff4757; -fx-font-weight: bold;");
        statusLabel.setText(msg);
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root, 450, 550));
            stage.setTitle("Platform Gateway Authentication");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}