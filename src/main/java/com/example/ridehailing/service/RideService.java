package com.capstone.ridehail.service;

import com.capstone.ridehail.model.*;

public class RideService {
    public void executeRideFlow() {
        System.out.println("=== RIDE FLOW START ===");

        // 1. Instantiating Entities
        Passenger passenger = new Passenger(101, "Gyro Radaza", "09123456789", "gyro@gmail.com");
        Driver driver = new Driver(201, "John Doe", "09876543210", "Toyota Vios");

        // 2. Passenger Books a Ride (Activity Diagram: Enter Pickup & Destination -> Book Ride)
        Ride ride = new Ride(5001, "Cebu IT Park", "SM Seaside");
        passenger.bookRide(ride);

        // 3. Driver receives and accepts the booking (Sequence Diagram)
        driver.acceptRide(ride);
        ride.updateStatus("Accepted");

        // 4. Ride Start & Complete (Sequence: Board Vehicle -> Ride in Progress -> Ride Complete)
        driver.startRide(ride);
        ride.updateStatus("In Progress");

        driver.completeRide(ride);
        ride.updateStatus("Completed");

        // 5. Payment processing
        Payment payment = new Payment(9001, ride.calculateFare(), "Cashless");
        passenger.makePayment(payment);
        payment.processPayment();

        // 6. Rating (Sequence: Submit Driver Rating)
        passenger.rateDriver(driver, 5);

        System.out.println("=== RIDE FLOW END ===");
    }
}