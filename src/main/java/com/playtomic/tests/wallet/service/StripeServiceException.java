package com.playtomic.tests.wallet.service;

public class StripeServiceException extends RuntimeException {
    public StripeServiceException(String message) {
        super(message);
    }
    public StripeServiceException() {
        super(); 
    }
}
