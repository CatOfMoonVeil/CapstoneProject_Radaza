package com.example.ridehailing.ui;

import com.capstone.ridehail.service.RideService;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println("Initializing Capstone Ride-Hailing Application...");

        // Execute the mock service to verify the flow logic is correct
        RideService service = new RideService();
        service.executeRideFlow();
    }
}