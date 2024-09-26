package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.WalletActions;
import com.playtomic.tests.wallet.repository.WalletActionsRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private StripePaymentService stripePaymentService;

    @Autowired
    private WalletActionsRepository walletActionsRepository;

    public synchronized Wallet topUpWallet(Long walletId, BigDecimal amount, String creditCard) {
        Wallet wallet = getWalletById(walletId); // Ensure wallet exists, otherwise throw exception
        stripePaymentService.charge(creditCard, amount); // Ensure Stripe payment is handled properly
        wallet.setBalance(wallet.getBalance().add(amount)); // Update wallet balance
    
        WalletActions aWalletActions = new WalletActions(walletId, "TOPUP", amount, LocalDateTime.now());
        walletActionsRepository.save(aWalletActions); // Log the action
    
        return walletRepository.save(wallet); // Save the updated wallet
    }
    
    
    public Wallet spendFromWallet(Long walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance to make the purchase");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));

        WalletActions aWalletActions = new WalletActions(walletId, "SPEND", amount, LocalDateTime.now());
        walletActionsRepository.save(aWalletActions);

        return walletRepository.save(wallet);
    }

    public Wallet refundToWallet(Long walletId, BigDecimal amount, String paymentId) {
        Wallet wallet = getWalletById(walletId);
        stripePaymentService.refund(paymentId);
        wallet.setBalance(wallet.getBalance().add(amount));

        WalletActions aWalletActions = new WalletActions(walletId, "REFUND", amount, LocalDateTime.now());
        walletActionsRepository.save(aWalletActions);

        return walletRepository.save(wallet);
    }

    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }

}
