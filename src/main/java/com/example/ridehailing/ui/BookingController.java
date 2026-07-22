package com.example.ridehailing.ui;

import com.example.ridehailing.model.*;
import com.example.ridehailing.util.UserSession;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookingController {

    @FXML private TextField pickupField;
    @FXML private TextField destinationField;
    @FXML private Button bookButton;
    @FXML private Button cancelButton;
    @FXML private Button logoutButton;
    @FXML private TextArea statusLogs;

    private Passenger currentPassenger;
    private Ride activeRide;
    private Driver assignedDriver;
    private Task<Void> rideSequenceTask;

    @FXML
    public void initialize() {
        currentPassenger = UserSession.getLoggedInPassenger();

        if (currentPassenger == null) {
            currentPassenger = new Passenger(9999, "Guest Profile", "00000", "guest@ride.com");
        }

        log("System Initialized.");
        log("Active User Profile: " + currentPassenger.getName());
        log("Welcome, " + currentPassenger.getName() + "!");
    }

    @FXML
    private void handleBookRide() {
        String pickup = pickupField.getText().trim();
        String destination = destinationField.getText().trim();

        if (pickup.isEmpty() || destination.isEmpty()) {
            log("[System Warning] Form incomplete. Specify location endpoints.");
            return;
        }

        activeRide = new Ride(7701, pickup, destination);
        currentPassenger.bookRide(activeRide);

        toggleFormState(true);
        statusLogs.clear();

        log("--- Sequence: Ride Request Registered ---");
        log("From: " + activeRide.getPickupLocation() + " -> To: " + activeRide.getDestination());
        log(String.format("Calculated Fare Estimate: $%.2f", activeRide.getFare()));

        rideSequenceTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateLogAsync("\n--- Sequence: Driver Assignment Engine ---");
                updateLogAsync("System: Querying random available driver unit... (Standby)");

                for (int i = 2; i > 0; i--) {
                    if (isCancelled()) return null;
                    Thread.sleep(1000);
                }

                // Queries database randomly via ORDER BY RAND()
                assignedDriver = UserSession.fetchAvailableDriver();

                if (assignedDriver == null) {
                    updateLogAsync("[Database System Message] No available drivers found. Request failed.");
                    cancel();
                    return null;
                }

                updateLogAsync("Database Log -> Retrieved Driver ID #" + assignedDriver.getDriverId());
                updateLogAsync("Assigned Unit: " + assignedDriver.getName() + " driving " + assignedDriver.getVehicle());
                assignedDriver.acceptRide(activeRide);

                updateLogAsync("\n--- Sequence: Transit Execution ---");
                updateLogAsync("Driver Contact: " + assignedDriver.getPhone());
                updateLogAsync("Driver: Arrived at " + activeRide.getPickupLocation());
                activeRide.updateStatus("In Progress");
                assignedDriver.startRide(activeRide);

                for (int i = 2; i > 0; i--) {
                    if (isCancelled()) return null;
                    updateLogAsync("En route to destination...");
                    Thread.sleep(1000);
                }

                if (isCancelled()) return null;

                assignedDriver.completeRide(activeRide);
                activeRide.updateStatus("Completed");

                updateLogAsync("\n--- Sequence: Billing Gateway ---");
                Payment transaction = new Payment(9981, activeRide.getFare(), "Digital Wallet");
                currentPassenger.makePayment(transaction);
                transaction.processPayment();

                updateLogAsync(String.format("Gateway Status: Payment Verified [$%.2f charged]", activeRide.getFare()));

                return null;
            }
        };

        rideSequenceTask.setOnSucceeded(e -> Platform.runLater(() -> {
            toggleFormState(false);
            promptDriverRating(assignedDriver); // Shows rating popup dialog
        }));

        rideSequenceTask.setOnFailed(e -> Platform.runLater(() -> {
            log("[System Error] Background logic sequence failed.");
            toggleFormState(false);
        }));

        rideSequenceTask.setOnCancelled(e -> Platform.runLater(() -> toggleFormState(false)));

        Thread backgroundThread = new Thread(rideSequenceTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    /**
     * Interactive 1-5 Star Rating Dialog Box
     */
    private void promptDriverRating(Driver driver) {
        if (driver == null) return;

        List<String> ratingOptions = Arrays.asList(
                "5 Stars - Excellent service!",
                "4 Stars - Good trip",
                "3 Stars - Average experience",
                "2 Stars - Room for improvement",
                "1 Star - Poor service"
        );

        ChoiceDialog<String> dialog = new ChoiceDialog<>("5 Stars - Excellent service!", ratingOptions);
        dialog.setTitle("Rate Your Driver");
        dialog.setHeaderText("Trip Completed with " + driver.getName() + "!");
        dialog.setContentText("How would you rate your driver (" + driver.getVehicle() + ")?");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String selected = result.get();
            int stars = Character.getNumericValue(selected.charAt(0));

            currentPassenger.rateDriver(driver, stars);
            log(String.format("\n--- Rating Submitted ---\nYou rated %s: %d/5 Stars. Thank you!", driver.getName(), stars));
        } else {
            log("\n--- Rating Skipped ---\nSystem: Transaction complete. Standby mode active.");
        }
    }

    @FXML
    private void handleCancelRide() {
        if (rideSequenceTask != null && rideSequenceTask.isRunning()) {
            rideSequenceTask.cancel();

            if (activeRide != null) {
                currentPassenger.cancelRide(activeRide);
            }

            log("\n[Abort Triggered] Active session aborted by Passenger request.");
            log("System: Clearing pipeline buffers... Resetting layout interface.");
            toggleFormState(false);
        }
    }

    @FXML
    private void handleLogOut() {
        if (rideSequenceTask != null && rideSequenceTask.isRunning()) {
            rideSequenceTask.cancel();
        }

        UserSession.logout();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ridehailing/ui/LoginView.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot, 450, 550));
            stage.setTitle("GoRide Platform Login");
        } catch (IOException e) {
            log("[System Error] Failed to return to authentication terminal.");
            e.printStackTrace();
        }
    }

    private void log(String message) {
        statusLogs.appendText(message + "\n");
    }

    private void updateLogAsync(String message) {
        Platform.runLater(() -> log(message));
    }

    private void toggleFormState(boolean working) {
        pickupField.setDisable(working);
        destinationField.setDisable(working);
        bookButton.setDisable(working);
        cancelButton.setDisable(!working);
        logoutButton.setDisable(working);
    }
}