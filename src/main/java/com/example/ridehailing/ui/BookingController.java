package com.example.ridehailing.ui;

import com.example.ridehailing.model.*;
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

    @FXML
    public void initialize() {
        // Mock setups matching your Activity flow instantiation steps
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

        toggleFormState(true);

        // Sequence Step 1: Request Ride & Log
        activeRide = new Ride(7701, pickup, destination);
        currentPassenger.bookRide(activeRide);
        log("\n--- Sequence: Ride Request Registered ---");
        log("From: " + activeRide.getPickupLocation() + " -> To: " + activeRide.getDestination());
        log("Calculated Fare Estimate: $" + activeRide.getFare());

        // Sequence Step 2: Driver Allocation
        log("\n--- Sequence: Driver Assignment Engine ---");
        log("System: Scanning active vehicle grid...");
        assignedDriver.acceptRide(activeRide);
        log("Assigned Unit: " + assignedDriver.getName() + " running a " + assignedDriver.getVehicle());

        // Sequence Step 3 & 4: Trip Progress
        log("\n--- Sequence: Transit Execution ---");
        log("Driver: Unit arrived at " + activeRide.getPickupLocation());
        activeRide.updateStatus("In Progress");
        assignedDriver.startRide(activeRide);

        log("...");

        // Sequence Step 5: Wrap up Trip
        assignedDriver.completeRide(activeRide);
        activeRide.updateStatus("Completed");

        // Sequence Step 6: Payment Transaction handling
        log("\n--- Sequence: Billing Gateway ---");
        Payment transaction = new Payment(9981, activeRide.getFare(), "Digital Wallet");
        currentPassenger.makePayment(transaction);
        transaction.processPayment();
        log("Gateway Status: Payment Verified [" + transaction.getPaymentStatus() + "]");

        // Sequence Step 7: Feedback Capture
        log("\n--- Sequence: Rating & Dispatch ---");
        currentPassenger.rateDriver(assignedDriver, 5);
        log("System: Feedback registered. Driver rating profile refreshed.");
        log("Status: Standby mode. Ready for subsequent bookings.");

        toggleFormState(false);
    }

    @FXML
    private void handleCancelRide() {
        if (activeRide != null) {
            currentPassenger.cancelRide(activeRide);
            log("[System Notice] Active session aborted by Passenger request.");
            toggleFormState(false);
        }
    }

    private void log(String message) {
        statusLogs.appendText(message + "\n");
    }

    private void toggleFormState(boolean working) {
        pickupField.setDisable(working);
        destinationField.setDisable(working);
        bookButton.setDisable(working);
        cancelButton.setDisable(!working);
    }
}