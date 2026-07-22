package com.example.ridehailing.ui;

import com.example.ridehailing.model.Driver;
import com.example.ridehailing.model.Passenger;
import com.example.ridehailing.util.UserSession;
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
            errorLabel.setStyle("-fx-text-fill: #ff4757;");
            errorLabel.setText("Please complete the form fields.");
            return;
        }

        String role = UserSession.checkUserRoleAndLogin(usernameInput, passwordInput);

        if ("PASSENGER".equals(role)) {
            Passenger matchedPassenger = UserSession.loginPassenger(usernameInput, passwordInput);
            if (matchedPassenger != null) {
                navigateToBooking();
            } else {
                showErrorMessage();
            }
        } else if ("DRIVER".equals(role)) {
            Driver matchedDriver = UserSession.loginDriver(usernameInput, passwordInput);
            if (matchedDriver != null) {
                navigateToDriverDashboard();
            } else {
                showErrorMessage();
            }
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        errorLabel.setStyle("-fx-text-fill: #ff4757;");
        errorLabel.setText("Account profile not found or invalid password.");
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

    @FXML
    private void handleNavigateToDriverSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/DriverSignUpView.fxml"));
            Parent signUpRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot, 450, 580));
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
            stage.setScene(new Scene(bookingRoot, 450, 580));
            stage.setTitle("GoRide Terminal Panel");
        } catch (IOException e) {
            errorLabel.setText("Failed to transition navigation screens.");
            e.printStackTrace();
        }
    }

    private void navigateToDriverDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/DriverDashboardView.fxml"));
            Parent driverRoot = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(driverRoot, 450, 550));
            stage.setTitle("GoRide Driver Command Center");
        } catch (IOException e) {
            errorLabel.setStyle("-fx-text-fill: #ff4757;");
            errorLabel.setText("Failed to open Driver command center.");
            e.printStackTrace();
        }
    }
}