package com.example.ridehailing.util;

import com.example.ridehailing.model.Payment;

public class PaymentAdapter extends Payment {
    private ThirdPartyPaymentApi externalApi;

    public PaymentAdapter(int paymentId, double amount, String paymentMethod) {
        super(paymentId, amount, paymentMethod);
        this.externalApi = new ThirdPartyPaymentApi();
    }

    @Override
    public void processPayment() {
        // Adapts system amount ($) to third-party requirements (cents)
        double cents = getAmount() * 100.0;
        boolean success = externalApi.executeCharge(cents, "TOK_GIDE_" + System.currentTimeMillis());

        if (success) {
            System.out.println("[PaymentAdapter] Adapted transaction successfully processed!");
        }
    }
}