package com.example.ridehailing.model;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    // Shared list of registered passengers
    private static final List<Passenger> registeredPassengers = new ArrayList<>();

    // Tracks who is currently logged in
    private static Passenger loggedInPassenger;

    static {
        // Seed database with default accounts
        registeredPassengers.add(new Passenger(1001, "Gyro Radaza", "09123456789", "gyro@gmail.com"));
        registeredPassengers.add(new Passenger(1002, "Alice Smith", "09223334444", "alice@gmail.com"));
    }

    public static List<Passenger> getRegisteredPassengers() {
        return registeredPassengers;
    }

    public static void registerPassenger(Passenger passenger) {
        registeredPassengers.add(passenger);
    }

    public static Passenger getLoggedInPassenger() {
        return loggedInPassenger;
    }

    public static void setLoggedInPassenger(Passenger passenger) {
        loggedInPassenger = passenger;
    }
}