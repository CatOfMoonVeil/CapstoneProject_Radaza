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

        Passenger matchedPassenger = UserSession.login(usernameInput, passwordInput);

        if (matchedPassenger != null) {
            UserSession.setLoggedInPassenger(matchedPassenger);
            navigateToBooking();
        } else {
            errorLabel.setText("Account profile not found or invalid password.");
        }
    }

    @FXML
    private void handleNavigateToSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/SignUpView.fxml"));
            Parent signUpRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot, 450, 550));
            stage.setTitle("Platform User Registration");
        } catch (IOException e) {
            errorLabel.setText("Could not open registration portal.");
            e.printStackTrace();
        }
    }

    // New navigation handler to open the Driver Sign-Up screen
    @FXML
    private void handleNavigateToDriverSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/DriverSignUpView.fxml"));
            Parent signUpRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot, 450, 580)); // Height adjusted to 580 to fit the vehicle info field
            stage.setTitle("Driver Registration");
        } catch (IOException e) {
            errorLabel.setText("Could not open driver registration.");
            e.printStackTrace();
        }
    }

    private void navigateToBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/BookingView.fxml"));
            Parent bookingRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(bookingRoot, 450, 550));
            stage.setTitle("GoRide Terminal Panel");
        } catch (IOException e) {
            errorLabel.setText("Failed to transition navigation screens.");
            e.printStackTrace();
        }
    }
}