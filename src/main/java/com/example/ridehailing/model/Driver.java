package com.capstone.ridehail.model;

public class Driver {
    private int driverId;
    private String name;
    private String phone;
    private String vehicle;
    private String status; // e.g., "Available", "Busy", "Offline"

    public Driver(int driverId, String name, String phone, String vehicle) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.vehicle = vehicle;
        this.status = "Available";
    }

    // Methods from Class Diagram
    public void acceptRide(Ride ride) {
        this.status = "Busy";
        System.out.println("Driver " + name + " accepted ride " + ride.getRideId());
    }

    public void rejectRide(Ride ride) {
        System.out.println("Driver " + name + " rejected ride " + ride.getRideId());
    }

    public void startRide(Ride ride) {
        System.out.println("Driver " + name + " started the trip.");
    }

    public void completeRide(Ride ride) {
        this.status = "Available";
        System.out.println("Driver " + name + " completed the trip.");
    }

    // Getters and Setters
    public String getName() { return name; }
}