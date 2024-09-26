package com.playtomic.tests.wallet.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.playtomic.tests.wallet.model.WalletActions;

@DataJpaTest
public class WalletActionsRepositoryTest {

    @Autowired
    private WalletActionsRepository walletActionsRepository;

    @Test
    public void testCreateWalletAction() {
        
        WalletActions action = new WalletActions(1L, "TOPUP", BigDecimal.valueOf(100.00), LocalDateTime.now());
        WalletActions savedAction = walletActionsRepository.save(action);

        
        Assertions.assertNotNull(savedAction.getId());
        Assertions.assertEquals("TOPUP", savedAction.getActionType());
        Assertions.assertEquals(BigDecimal.valueOf(100.00), savedAction.getAmount());
    }

    @Test
    public void testReadWalletAction() {
       
        WalletActions action = new WalletActions(1L, "SPEND", BigDecimal.valueOf(50.00), LocalDateTime.now());
        WalletActions savedAction = walletActionsRepository.save(action);

        
        Optional<WalletActions> retrievedAction = walletActionsRepository.findById(savedAction.getId());

        
        Assertions.assertTrue(retrievedAction.isPresent());
        Assertions.assertEquals(savedAction.getAmount(), retrievedAction.get().getAmount());
    }

    @Test
    public void testUpdateWalletAction() {
        
        WalletActions action = new WalletActions(1L, "REFUND", BigDecimal.valueOf(75.00), LocalDateTime.now());
        WalletActions savedAction = walletActionsRepository.save(action);

        
        savedAction.setAmount(BigDecimal.valueOf(100.00));
        WalletActions updatedAction = walletActionsRepository.save(savedAction);
        
        Assertions.assertEquals(BigDecimal.valueOf(100.00), updatedAction.getAmount());
    }

    @Test
    public void testDeleteWalletAction() {
        
        WalletActions action = new WalletActions(1L, "SPEND", BigDecimal.valueOf(30.00), LocalDateTime.now());
        WalletActions savedAction = walletActionsRepository.save(action);
       
        walletActionsRepository.deleteById(savedAction.getId());
       
        Optional<WalletActions> deletedAction = walletActionsRepository.findById(savedAction.getId());
        Assertions.assertFalse(deletedAction.isPresent());
    }

    @Test
    public void testFindActionsByWalletId() {
        
        WalletActions action1 = new WalletActions(1L, "TOPUP", BigDecimal.valueOf(100.00), LocalDateTime.now());
        WalletActions action2 = new WalletActions(1L, "SPEND", BigDecimal.valueOf(50.00), LocalDateTime.now());
        walletActionsRepository.save(action1);
        walletActionsRepository.save(action2);
       
        List<WalletActions> actions = walletActionsRepository.findByWalletId(1L);
       
        Assertions.assertEquals(4, actions.size());
    }
}
