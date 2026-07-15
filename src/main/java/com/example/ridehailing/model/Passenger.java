package com.capstone.ridehail.model;

public class Passenger {
    private int passengerId;
    private String name;
    private String phone;
    private String email;

    public Passenger(int passengerId, String name, String phone, String email) {
        this.passengerId = passengerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Methods from Class Diagram
    public void bookRide(Ride ride) {
        System.out.println(name + " requested a ride to " + ride.getDestination());
    }

    public void cancelRide(Ride ride) {
        System.out.println(name + " canceled the ride.");
    }

    public void makePayment(Payment payment) {
        System.out.println(name + " is processing a payment of $" + payment.getAmount());
    }

    public void rateDriver(Driver driver, int rating) {
        System.out.println(name + " rated driver " + driver.getName() + " with " + rating + " stars.");
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getPassengerId() { return passengerId; }
}