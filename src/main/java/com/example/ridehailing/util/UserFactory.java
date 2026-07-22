package com.example.ridehailing.util;

import com.example.ridehailing.model.Admin;
import com.example.ridehailing.model.Driver;
import com.example.ridehailing.model.Passenger;
import com.example.ridehailing.model.User;

public class UserFactory {

    /**
     * Factory method to instantiate specific User subclasses dynamically based on role.
     */
    public static User createUser(String role, int id, String name, String phone, String email, String vehicle) {
        if (role == null) {
            return null;
        }

        switch (role.toUpperCase()) {
            case "PASSENGER":
                return new Passenger(id, name, phone, email);
            case "DRIVER":
                return new Driver(id, name, phone, vehicle != null ? vehicle : "Standard Vehicle");
            case "ADMIN":
                return new Admin(id, name, phone, email);
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }
}