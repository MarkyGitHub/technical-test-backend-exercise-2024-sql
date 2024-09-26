package com.playtomic.tests.wallet.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.playtomic.tests.wallet.model.Wallet;

@DataJpaTest
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    public void testCreateWallet() {
       
        Wallet wallet = new Wallet(BigDecimal.valueOf(100.00));
        Wallet savedWallet = walletRepository.save(wallet);

        Assertions.assertNotNull(savedWallet.getId());
        Assertions.assertEquals(BigDecimal.valueOf(100.00), savedWallet.getBalance());
    }

    @Test
    public void testReadWallet() {
        
        Wallet wallet = new Wallet(BigDecimal.valueOf(200.00));
        Wallet savedWallet = walletRepository.save(wallet);

        Optional<Wallet> retrievedWallet = walletRepository.findById(savedWallet.getId());

        Assertions.assertTrue(retrievedWallet.isPresent());
        Assertions.assertEquals(savedWallet.getBalance(), retrievedWallet.get().getBalance());
    }

    @Test
    public void testUpdateWallet() {
       
        Wallet wallet = new Wallet(BigDecimal.valueOf(150.00));
        Wallet savedWallet = walletRepository.save(wallet);

        savedWallet.setBalance(BigDecimal.valueOf(250.00));
        Wallet updatedWallet = walletRepository.save(savedWallet);

        Assertions.assertEquals(BigDecimal.valueOf(250.00), updatedWallet.getBalance());
    }

    @Test
    public void testDeleteWallet() {
        
        Wallet wallet = new Wallet(BigDecimal.valueOf(50.00));
        Wallet savedWallet = walletRepository.save(wallet);

        walletRepository.deleteById(savedWallet.getId());

        Optional<Wallet> deletedWallet = walletRepository.findById(savedWallet.getId());
        Assertions.assertFalse(deletedWallet.isPresent());
    }
}
