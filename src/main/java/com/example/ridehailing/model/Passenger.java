package com.example.ridehailing.model;

import java.io.Serializable;

public class Passenger extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int passengerId;

    public Passenger(int passengerId, String name, String phone, String email) {
        super(name, phone, email);
        this.passengerId = passengerId;
    }

    public void bookRide(Ride ride) {
        System.out.println("Passenger " + name + " requested a ride to " + ride.getDestination());
    }

    public void cancelRide(Ride ride) {
        System.out.println("Passenger " + name + " canceled the ride request.");
    }

    public void makePayment(Payment payment) {
        System.out.println("Passenger " + name + " is authorizing a payment of $" + payment.getAmount());
    }

    public void rateDriver(Driver driver, int rating) {
        System.out.println("Passenger " + name + " rated Driver " + driver.getName() + ": " + rating + " stars.");
    }

    public int getPassengerId() { return passengerId; }
}