package com.example.ridehailing.model;

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
        this.fare = calculateFare();
    }

    public double calculateFare() {
        // Base structure mimicking your class diagram's method
        return 15.50;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public int getRideId() { return rideId; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDestination() { return destination; }
    public double getFare() { return fare; }
    public String getStatus() { return status; }
}