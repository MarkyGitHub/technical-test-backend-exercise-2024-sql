package com.playtomic.tests.wallet.service;

public class BaseWalletException extends RuntimeException {
    
    public BaseWalletException(String message) {
        super(message);
    }
    public BaseWalletException() {
        super(); 
    }
}
