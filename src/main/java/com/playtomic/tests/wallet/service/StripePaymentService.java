package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    public void charge(String creditCardNumber, BigDecimal amount) {
        // Simulated call to Stripe payment gateway
        if (amount.compareTo(BigDecimal.TEN) < 0) {
            throw new RuntimeException("Amount too small");
        }
        // In a real application, this would call Stripe's API
        System.out.println("Charged " + amount + " to credit card: " + creditCardNumber);
    }

    public void refund(String paymentId) {
        // Simulated call to Stripe refund API
        System.out.println("Refunded payment with ID: " + paymentId);
    }
}
