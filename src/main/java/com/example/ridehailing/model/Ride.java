package com.capstone.ridehail.model;

public class Ride {
    private int rideId;
    private String pickupLocation;
    private String destination;
    private double fare;
    private String status; // "Requested", "Accepted", "In Progress", "Completed"

    public Ride(int rideId, String pickupLocation, String destination) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.status = "Requested";
        this.fare = calculateFare();
    }

    public double calculateFare() {
        // Simple mock calculation for now
        return 15.00;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    // Getters
    public int getRideId() { return rideId; }
    public String getDestination() { return destination; }
    public double getFare() { return fare; }
}