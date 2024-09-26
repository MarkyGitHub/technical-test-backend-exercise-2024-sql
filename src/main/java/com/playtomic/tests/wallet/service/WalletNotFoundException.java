package com.playtomic.tests.wallet.service;

public class WalletNotFoundException extends RuntimeException {
    
    public WalletNotFoundException(String message) {
        super(message);
    }
    public WalletNotFoundException() {
        super(); 
    }
}
