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
    private Driver assignedDriver;
    private Ride activeRide;

    // Track the background background thread task so we can cancel it mid-execution
    private Task<Void> rideSequenceTask;

    @FXML
    public void initialize() {
        currentPassenger = new Passenger(1001, "Gyro Radaza", "09123456789", "gyro.radaza@example.com");
        assignedDriver = new Driver(2001, "John Doe", "09876543210", "Toyota Vios (Plate: ABC-123)");

        log("System Initialized.");
        log("Welcome back, " + currentPassenger.getName() + "!");
    }

    @FXML
    private void handleBookRide() {
        String pickup = pickupField.getText().trim();
        String destination = destinationField.getText().trim();

        if (pickup.isEmpty() || destination.isEmpty()) {
            log("[System Warning] Form incomplete. Specify location endpoints.");
            return;
        }

        // 1. Instantiation & Setup UI constraints
        activeRide = new Ride(7701, pickup, destination);
        currentPassenger.bookRide(activeRide);

        toggleFormState(true);
        statusLogs.clear();

        log("--- Sequence: Ride Request Registered ---");
        log("From: " + activeRide.getPickupLocation() + " -> To: " + activeRide.getDestination());
        log("Calculated Fare Estimate: $" + activeRide.getFare());

        // 2. Define the asynchronous timeline sequence
        rideSequenceTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Step A: Simulated Allocation Search Delay
                updateLogAsync("\n--- Sequence: Driver Assignment Engine ---");
                updateLogAsync("System: Scanning active vehicle grid... (Waiting for driver to accept)");

                // 3 Second Timed Wait Loop
                for (int i = 3; i > 0; i--) {
                    if (isCancelled()) return null;
                    updateLogAsync("Searching... (" + i + "s remaining)");
                    Thread.sleep(1000);
                }

                if (isCancelled()) return null;

                // Step B: Driver found & processes step
                updateLogAsync("Assigned Unit: " + assignedDriver.getName() + " running a " + assignedDriver.getVehicle());
                assignedDriver.acceptRide(activeRide);

                // Step C: Transit execution delays
                updateLogAsync("\n--- Sequence: Transit Execution ---");
                updateLogAsync("Driver: Unit arrived at " + activeRide.getPickupLocation());
                activeRide.updateStatus("In Progress");
                assignedDriver.startRide(activeRide);

                for (int i = 2; i > 0; i--) {
                    if (isCancelled()) return null;
                    updateLogAsync("En route to destination...");
                    Thread.sleep(1000);
                }

                if (isCancelled()) return null;

                // Step D: Finish ride transaction elements
                assignedDriver.completeRide(activeRide);
                activeRide.updateStatus("Completed");

                // Step E: Billing Gateway
                updateLogAsync("\n--- Sequence: Billing Gateway ---");
                Payment transaction = new Payment(9981, activeRide.getFare(), "Digital Wallet");
                currentPassenger.makePayment(transaction);
                transaction.processPayment();
                updateLogAsync("Gateway Status: Payment Verified [" + transaction.getPaymentStatus() + "]");

                // Step F: Feedback Capture
                updateLogAsync("\n--- Sequence: Rating & Dispatch ---");
                currentPassenger.rateDriver(assignedDriver, 5);
                updateLogAsync("System: Transaction complete. Standby mode active.");

                return null;
            }
        };

        // Re-enable UI components when background work finishes cleanly
        rideSequenceTask.setOnSucceeded(e -> toggleFormState(false));

        // Manage cleanup explicitly if the thread task fails unexpectedly
        rideSequenceTask.setOnFailed(e -> {
            log("[System Error] Background logic sequence failed.");
            toggleFormState(false);
        });

        // Run the task on a separate execution thread background pool
        Thread backgroundThread = new Thread(rideSequenceTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    @FXML
    private void handleCancelRide() {
        if (rideSequenceTask != null && rideSequenceTask.isRunning()) {
            // Terminate background processing flags
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

    // Helper utility to safe-push UI updates across background threads
    private void updateLogAsync(String message) {
        Platform.runLater(() -> log(message));
    }

    private void toggleFormState(boolean working) {
        pickupField.setDisable(working);
        destinationField.setDisable(working);
        bookButton.setDisable(working);
        cancelButton.setDisable(!working); // "Abort Request" becomes clickable during processing
    }
}