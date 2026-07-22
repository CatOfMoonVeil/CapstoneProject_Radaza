package com.example.ridehailing.model;

public interface RideObserver {
    void onRideStatusChanged(String status);
}