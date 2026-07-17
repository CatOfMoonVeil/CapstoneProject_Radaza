package com.example.ridehailing.model;

import java.util.Random;

public class Ride {
    private int rideId;
    private String pickupLocation;
    private String destination;
    private double fare;
    private String status;

    public Ride(int rideId, String pickupLocation, String destination) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.status = "Requested";
        this.fare = calculateFare(); // Calculates a dynamic random fare value
    }

    public double calculateFare() {
        Random random = new Random();
        double min = 1.00;
        double max = 200.99;
        double randomValue = min + (max - min) * random.nextDouble();

        return Math.round(randomValue * 100.0) / 100.0;
    }

    public int getRideId() { return rideId; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDestination() { return destination; }
    public double getFare() { return fare; }
    public String getStatus() { return status; }
    public void updateStatus(String status) { this.status = status; }
}