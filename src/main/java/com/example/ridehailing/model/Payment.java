package com.capstone.ridehail.model;

public class Payment {
    private int paymentId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;

    public Payment(int paymentId, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "Pending";
    }

    public void processPayment() {
        this.paymentStatus = "Successful";
        System.out.println("Payment of $" + amount + " processed successfully via " + paymentMethod);
    }

    public void refundPayment() {
        this.paymentStatus = "Refunded";
        System.out.println("Payment refunded.");
    }

    public double getAmount() { return amount; }
}