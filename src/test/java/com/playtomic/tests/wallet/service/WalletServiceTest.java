package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.WalletActions;
import com.playtomic.tests.wallet.repository.WalletActionsRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;

class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private StripePaymentService stripePaymentService;

    @Mock
    private WalletActionsRepository walletActionsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopUpWallet() {
        // Given
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100.00);
        String creditCard = "4111111111111111";

        Wallet wallet = new Wallet(BigDecimal.valueOf(50.00));
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet); // Mock save operation to return wallet

        // When
        Wallet updatedWallet = walletService.topUpWallet(walletId, amount, creditCard);

        // Then
        assertEquals(BigDecimal.valueOf(150.00), updatedWallet.getBalance()); // Check if balance updated
        verify(stripePaymentService).charge(creditCard, amount); // Verify payment was processed
        verify(walletActionsRepository).save(any(WalletActions.class)); // Verify that WalletActions were saved
        verify(walletRepository).save(wallet); // Verify wallet is saved after top-up
    }

    @Test
    void testSpendFromWallet_withSufficientBalance() {
        // Given
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(30.00);

        Wallet wallet = new Wallet(BigDecimal.valueOf(100.00));
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet); // Mock save operation to return wallet

        // When
        Wallet updatedWallet = walletService.spendFromWallet(walletId, amount);

        // Then
        assertEquals(BigDecimal.valueOf(70.00), updatedWallet.getBalance()); // Check if balance was subtracted
        verify(walletActionsRepository).save(any(WalletActions.class)); // Verify WalletActions was saved
        verify(walletRepository).save(wallet); // Verify wallet is saved after spending
    }

    @Test
    void testSpendFromWallet_withInsufficientBalance() {
        // Given
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(150.00);

        Wallet wallet = new Wallet(BigDecimal.valueOf(100.00));
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When and Then
        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            walletService.spendFromWallet(walletId, amount);
        });

        assertEquals("Insufficient balance to make the purchase", exception.getMessage());
        verify(walletActionsRepository, never()).save(any(WalletActions.class)); // No transaction saved
        verify(walletRepository, never()).save(wallet); // Wallet should not be saved
    }

    @Test
    void testRefundToWallet() {
        // Given
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(50.00);
        String paymentId = "payment_123";

        Wallet wallet = new Wallet(BigDecimal.valueOf(200.00));
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet); // Mock save operation to return wallet

        // When
        Wallet updatedWallet = walletService.refundToWallet(walletId, amount, paymentId);

        // Then
        assertEquals(BigDecimal.valueOf(250.00), updatedWallet.getBalance()); // Check if balance increased
        verify(stripePaymentService).refund(paymentId); // Verify the refund was called
        verify(walletActionsRepository).save(any(WalletActions.class)); // Verify WalletActions was saved
        verify(walletRepository).save(wallet); // Verify wallet is saved after refund
    }

    @Test
    void testGetWalletById_WalletExists() {
        // Given
        Long walletId = 1L;
        Wallet wallet = new Wallet(BigDecimal.valueOf(100.00));
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When
        Wallet foundWallet = walletService.getWalletById(walletId);

        // Then
        assertNotNull(foundWallet);
        assertEquals(walletId, foundWallet.getId());
        verify(walletRepository).findById(walletId); // Verify the repository was called
    }

    @Test
    void testGetWalletById_WalletNotFound() {
        // Given
        Long walletId = 1L;

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        // When and Then
        Exception exception = assertThrows(WalletNotFoundException.class, () -> {
            walletService.getWalletById(walletId);
        });

        assertEquals("Wallet not found", exception.getMessage());
        verify(walletRepository).findById(walletId); // Verify the repository was called
    }
}
