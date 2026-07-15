package com.example.ridehailing.model;

public class Driver extends User {
    private int driverId;
    private String vehicle;
    private String status;

    public Driver(int driverId, String name, String phone, String vehicle) {
        super(name, phone, "");
        this.driverId = driverId;
        this.vehicle = vehicle;
        this.status = "Available";
    }

    public void acceptRide(Ride ride) {
        this.status = "Busy";
        System.out.println("Driver " + name + " accepted ride assignment #" + ride.getRideId());
    }

    public void rejectRide(Ride ride) {
        System.out.println("Driver " + name + " rejected ride assignment #" + ride.getRideId());
    }

    public void startRide(Ride ride) {
        System.out.println("Driver " + name + " started the trip to " + ride.getDestination());
    }

    public void completeRide(Ride ride) {
        this.status = "Available";
        System.out.println("Driver " + name + " arrived at destination. Trip complete.");
    }

    public int getDriverId() { return driverId; }
    public String getVehicle() { return vehicle; }
    public String getStatus() { return status; }
}