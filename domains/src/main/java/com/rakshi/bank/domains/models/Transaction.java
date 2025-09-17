package com.rakshi.bank.domains.models;

import com.rakshi.bank.domains.enums.TransactionStatus;
import com.rakshi.bank.domains.enums.TransactionType;
import com.rakshi.bank.domains.utils.CustomIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transaction_account", columnList = "accountId"),
        @Index(name = "idx_transaction_reference", columnList = "transactionReference", unique = true),
        @Index(name = "idx_transaction_date", columnList = "transactionDate"),
        @Index(name = "idx_transaction_type", columnList = "transactionType"),
        @Index(name = "idx_transaction_status", columnList = "status"),
        @Index(name = "idx_transaction_amount", columnList = "amount"),
        @Index(name = "idx_transaction_external_ref", columnList = "externalReference"),
        @Index(name = "idx_transaction_user", columnList = "initiatedBy")
})
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String transactionId;

    @Column(nullable = false, unique = true, length = 50)
    private String transactionReference;

    @Column(nullable = false)
    private String accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", insertable = false, updatable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String particulars; // Brief transaction details

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal balanceAfterTransaction;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private LocalDateTime valueDate; // When the transaction value takes effect

    // For transfer transactions
    private String toAccountId;
    private String toAccountNumber;
    private String toBeneficiaryName;
    private String toIfscCode;

    // External references
    @Column(length = 100)
    private String externalReference; // UTR, RRN, etc.

    @Column(length = 50)
    private String chequeNumber;

    @Column(length = 100)
    private String merchantName;

    @Column(length = 50)
    private String merchantCategory;

    // Transaction fees and charges
    @Column(precision = 10, scale = 2)
    private BigDecimal transactionFee;

    @Column(precision = 10, scale = 2)
    private BigDecimal gstOnFee;

    // Geo-location (for security)
    @Column(length = 50)
    private String transactionLocation;

    @Column(length = 50)
    private String deviceId;

    @Column(length = 100)
    private String ipAddress;

    // Processing details
    @Column(length = 50)
    private String processedBy; // ATM, BRANCH, ONLINE, MOBILE, etc.

    @Column(length = 100)
    private String processingBranch;

    @Column(length = 500)
    private String failureReason;

    private LocalDateTime processedAt;
    private LocalDateTime settledAt;

    @Column(nullable = false)
    private String initiatedBy; // User ID who initiated the transaction

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateIds() {
        if (this.transactionId == null) {
            this.transactionId = String.format("TXN%012d", CustomIdGenerator.ACCOUNT_ID_GENERATOR.incrementAndGet());
        }
        if (this.transactionReference == null) {
            this.transactionReference = "REF" + System.currentTimeMillis() + String.format("%04d", CustomIdGenerator.ACCOUNT_ID_GENERATOR.incrementAndGet() % 10000);
        }
        if (this.valueDate == null) {
            this.valueDate = this.transactionDate != null ? this.transactionDate : LocalDateTime.now();
        }
    }
}