package com.playtomic.tests.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.playtomic.tests.wallet.model.WalletActions;

@Repository
public interface WalletActionsRepository extends JpaRepository<WalletActions, Long> {
    List<WalletActions> findByWalletId(Long walletId);
}
