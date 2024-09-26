package com.playtomic.tests.wallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WalletActions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Ensure this is present
    private Long id;

    private Long walletId;
    private String actionType;
    private BigDecimal amount;
    private LocalDateTime created;

    public WalletActions() {}

    public WalletActions(Long walletId, String type, BigDecimal amount, LocalDateTime timestamp) {
        this.walletId = walletId;
        this.actionType = type;
        this.amount = amount;
        this.created = timestamp;
    }
}
