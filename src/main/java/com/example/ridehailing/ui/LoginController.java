package com.example.ridehailing.ui;

import com.example.ridehailing.model.Passenger;
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

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String usernameInput = usernameField.getText().trim();
        String passwordInput = passwordField.getText().trim();

        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            errorLabel.setText("Please complete the form fields.");
            return;
        }

        Passenger matchedPassenger = null;

        // Loop through the mock database array list for matching elements
        for (Passenger p : UserSession.getRegisteredPassengers()) {
            if (p.getEmail().equalsIgnoreCase(usernameInput) && passwordInput.equals("password123")) {
                matchedPassenger = p;
                break; // Break loop once an entry matches
            }
        }

        if (matchedPassenger != null) {
            // Set session variable so BookingController knows who is booking
            UserSession.setLoggedInPassenger(matchedPassenger);
            navigateToBooking();
        } else {
            errorLabel.setText("Account profile not found. Please Sign Up!");
        }
    }

    @FXML
    private void handleNavigateToSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUpView.fxml"));
            Parent signUpRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot, 450, 550));
            stage.setTitle("Platform User Registration");
        } catch (IOException e) {
            errorLabel.setText("Could not open registration portal.");
            e.printStackTrace();
        }
    }

    private void navigateToBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingView.fxml"));
            Parent bookingRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(bookingRoot, 450, 550));
            stage.setTitle("Capstone Ride-Hailing Platform Framework");
        } catch (IOException e) {
            errorLabel.setText("Failed to transition navigation screens.");
            e.printStackTrace();
        }
    }
}