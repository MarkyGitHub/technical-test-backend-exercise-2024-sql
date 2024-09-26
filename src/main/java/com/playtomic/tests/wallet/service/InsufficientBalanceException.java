package com.playtomic.tests.wallet.service;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
    public InsufficientBalanceException() {
        super(); 
    }
}
