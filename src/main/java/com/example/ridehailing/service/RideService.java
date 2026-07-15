package com.example.ridehailing.service;

import com.example.ridehailing.model.Ride;

public class RideService {
    public boolean processRouteCheck(Ride ride) {
        return ride.getPickupLocation() != null && !ride.getPickupLocation().isEmpty();
    }
}