package com.playtomic.tests.wallet.service;

public class StripeAmountTooSmallException extends StripeServiceException {

    public StripeAmountTooSmallException(String message) {
        super(message); 
    }
    public StripeAmountTooSmallException() {
        super(); 
    }
}
