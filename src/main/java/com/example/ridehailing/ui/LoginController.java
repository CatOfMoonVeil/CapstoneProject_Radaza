package com.example.ridehailing.ui;

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

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        if (username.equalsIgnoreCase("passenger") && password.equals("password123")) {
            navigateToBooking();
        } else {
            errorLabel.setText("Invalid credentials. Try passenger / password123");
        }
    }

    private void navigateToBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingView.fxml"));
            Parent bookingRoot = fxmlLoader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();

            Scene scene = new Scene(bookingRoot, 450, 550);
            stage.setScene(scene);
            stage.setTitle("Need a Ride?");
            stage.show();

        } catch (IOException e) {
            errorLabel.setText("bad navigation screen request, man... nice try.");
            e.printStackTrace();
        }
    }
}