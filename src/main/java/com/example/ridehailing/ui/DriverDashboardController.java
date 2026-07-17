package com.example.ridehailing.ui;

import com.example.ridehailing.model.Driver;
import com.example.ridehailing.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class DriverDashboardController {

    @FXML private Label driverWelcomeLabel;
    @FXML private Label vehicleLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea driverLogs;
    @FXML private Button toggleAvailabilityButton;

    private Driver currentDriver;
    private boolean isAvailable = true;

    @FXML
    public void initialize() {
        currentDriver = UserSession.fetchAvailableDriver();

        if (currentDriver != null) {
            driverWelcomeLabel.setText("Active Driver: " + currentDriver.getName());
            vehicleLabel.setText(currentDriver.getVehicle());
            log("System: Welcome to the GoRide network, " + currentDriver.getName());
            log("Status check: Active on vehicle database grid.");
        } else {
            driverWelcomeLabel.setText("Active Driver: Seeding Failure");
            vehicleLabel.setText("No Vehicle Registered");
            log("[Error] Failed to fetch active driver profile.");
        }
    }

    @FXML
    private void handleToggleAvailability() {
        if (isAvailable) {
            isAvailable = false;
            statusLabel.setText("OFFLINE - Standby");
            statusLabel.setStyle("-fx-text-fill: #ff4757; -fx-font-weight: bold;");
            toggleAvailabilityButton.setText("GO ONLINE");
            toggleAvailabilityButton.setStyle("-fx-background-color: #10ac84; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px;");
            log("System Action -> Status updated to OFFLINE. Booking dispatches paused.");
        } else {
            isAvailable = true;
            statusLabel.setText("ONLINE - Available");
            statusLabel.setStyle("-fx-text-fill: #10ac84; -fx-font-weight: bold;");
            toggleAvailabilityButton.setText("GO OFFLINE");
            toggleAvailabilityButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px;");
            log("System Action -> Status updated to ONLINE. Ready for passenger match cycles.");
        }
    }

    @FXML
    private void handleLogOut() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/LoginView.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage stage = (Stage) toggleAvailabilityButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot, 450, 550));
            stage.setTitle("GoRide Platform Login");
        } catch (IOException e) {
            log("[System Error] Failed to return to authentication terminal.");
            e.printStackTrace();
        }
    }

    private void log(String message) {
        driverLogs.appendText(message + "\n");
    }
}