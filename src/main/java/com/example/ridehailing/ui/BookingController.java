package com.example.ridehailing.ui;

import com.example.ridehailing.model.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BookingController {

    @FXML private TextField pickupField;
    @FXML private TextField destinationField;
    @FXML private Button bookButton;
    @FXML private Button cancelButton;
    @FXML private TextArea statusLogs;

    private Passenger currentPassenger;
    private Ride activeRide;
    private Task<Void> rideSequenceTask;

    @FXML
    public void initialize() {
        // Retrieve the passenger from the active session
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

        // Creates ride which internally generates a dynamic fare ($1.00 - $200.99)
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
                updateLogAsync("System: Querying active database grids... (Standby)");

                for (int i = 2; i > 0; i--) {
                    if (isCancelled()) return null;
                    Thread.sleep(1000);
                }

                // Query the database dynamically for an available driver
                Driver dbDriver = UserSession.fetchAvailableDriver();

                if (dbDriver == null) {
                    updateLogAsync("[Database System Message] No available drivers in this quadrant. Request failed.");
                    cancel();
                    return null;
                }

                // Log output from database records
                updateLogAsync("Database Log -> Retrieved Driver ID #" + dbDriver.getDriverId());
                updateLogAsync("Assigned Unit: " + dbDriver.getName() + " running a " + dbDriver.getVehicle());
                dbDriver.acceptRide(activeRide);

                updateLogAsync("\n--- Sequence: Transit Execution ---");
                updateLogAsync("Driver Info: Contact Unit at " + dbDriver.getPhone());
                updateLogAsync("Driver: Unit arrived at " + activeRide.getPickupLocation());
                activeRide.updateStatus("In Progress");
                dbDriver.startRide(activeRide);

                for (int i = 2; i > 0; i--) {
                    if (isCancelled()) return null;
                    updateLogAsync("En route to destination...");
                    Thread.sleep(1000);
                }

                if (isCancelled()) return null;

                dbDriver.completeRide(activeRide);
                activeRide.updateStatus("Completed");

                updateLogAsync("\n--- Sequence: Billing Gateway ---");
                Payment transaction = new Payment(9981, activeRide.getFare(), "Digital Wallet");
                currentPassenger.makePayment(transaction);
                transaction.processPayment();

                updateLogAsync(String.format("Gateway Status: Payment Verified [$%.2f charged]", activeRide.getFare()));

                updateLogAsync("\n--- Sequence: Rating & Dispatch ---");
                currentPassenger.rateDriver(dbDriver, 5);
                updateLogAsync("System: Transaction complete. Standby mode active.");

                return null;
            }
        };

        rideSequenceTask.setOnSucceeded(e -> toggleFormState(false));
        rideSequenceTask.setOnFailed(e -> {
            log("[System Error] Background logic sequence failed.");
            toggleFormState(false);
        });
        rideSequenceTask.setOnCancelled(e -> toggleFormState(false));

        Thread backgroundThread = new Thread(rideSequenceTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
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
    }
}