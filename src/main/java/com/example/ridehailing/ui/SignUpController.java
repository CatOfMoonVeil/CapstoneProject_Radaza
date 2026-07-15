package com.example.ridehailing.ui;

import com.example.ridehailing.model.Passenger;
import com.example.ridehailing.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Button signUpButton;
    @FXML private Label statusLabel;

    @FXML
    private void handleSignUp() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            statusLabel.setText("All registration fields are required!");
            return;
        }

        // Generate a random ID for the new Passenger instance
        int generatedId = 1000 + new Random().nextInt(9000);

        Passenger newPassenger = new Passenger(generatedId, name, phone, email);
        UserSession.registerPassenger(newPassenger);

        statusLabel.setText("Registration successful! Redirecting...");

        // Return back to Login screen automatically
        handleBackToLogin();
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root, 450, 550));
            stage.setTitle("Platform Gateway Authentication");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}