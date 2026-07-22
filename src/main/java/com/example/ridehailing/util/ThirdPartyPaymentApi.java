package com.example.ridehailing.util;

// Simulates a 3rd-party Payment Gateway API with an incompatible interface
public class ThirdPartyPaymentApi {
    public boolean executeCharge(double amountInCents, String token) {
        System.out.println("[3rd-Party Gateway] Executing charge of " + amountInCents + " cents using token: " + token);
        return true;
    }
}