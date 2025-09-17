package com.rakshi.bank.domains.models;

import com.rakshi.bank.domains.enums.AccountStatus;
import com.rakshi.bank.domains.enums.AccountType;
import com.rakshi.bank.domains.utils.ConvertBooleanAttributes;
import com.rakshi.bank.domains.utils.CustomIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "accounts", indexes = {
        @Index(name = "idx_account_number", columnList = "accountNumber", unique = true),
        @Index(name = "idx_account_user", columnList = "userId"),
        @Index(name = "idx_account_type", columnList = "accountType"),
        @Index(name = "idx_account_status", columnList = "status"),
        @Index(name = "idx_account_branch", columnList = "branchCode"),
        @Index(name = "idx_account_created", columnList = "createdAt"),
        @Index(name = "idx_account_balance", columnList = "currentBalance")
})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String accountId;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false)
    private String userId; // Foreign key to User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(nullable = false, length = 100)
    private String accountName;

    @Column(precision = 15, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal minimumBalance;

    @Column(precision = 15, scale = 2)
    private BigDecimal overdraftLimit;

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(length = 10)
    private String branchCode;

    @Column(length = 15)
    private String ifscCode;

    @Column(length = 50)
    private String currency = "INR";

    @Column(nullable = false)
    private LocalDate accountOpeningDate;

    private LocalDate accountClosingDate;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isJointAccount = false;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isMinorAccount = false;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isBusinessAccount = false;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isDormant = false;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean allowOnlineTransactions = true;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean allowInternationalTransactions = false;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean allowAtmTransactions = true;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean allowChequeTransactions = true;

    @Column(precision = 15, scale = 2)
    private BigDecimal dailyTransactionLimit;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyTransactionLimit;

    private Integer maxTransactionsPerDay;

    private LocalDateTime lastTransactionDate;
    private LocalDateTime lastDebitDate;
    private LocalDateTime lastCreditDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    // Relationships
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AccountHolder> accountHolders; // For joint accounts

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Beneficiary> beneficiaries;

    @PrePersist
    private void generateIds() {
        if (this.accountId == null) {
            this.accountId = String.format("ACC%010d", CustomIdGenerator.ACCOUNT_ID_GENERATOR.incrementAndGet());
        }
        if (this.accountNumber == null) {
            // Generate account number based on account type and branch
            String typeCode = switch (this.accountType) {
                case SAVINGS -> "10";
                case CHECKING -> "11";
                case CURRENT -> "20";
                case FIXED_DEPOSIT -> "30";
                case RECURRING_DEPOSIT -> "31";
                case LOAN -> "40";
                case CREDIT -> "50";
                default -> "00";
            };
            String branchPrefix = this.branchCode != null ? this.branchCode.substring(0, Math.min(4, this.branchCode.length())) : "0000";
            long sequence = CustomIdGenerator.ACCOUNT_ID_GENERATOR.get();
            this.accountNumber = branchPrefix + typeCode + String.format("%010d", sequence);
        }
        if (this.accountOpeningDate == null) {
            this.accountOpeningDate = LocalDate.now();
        }
        if (this.minimumBalance == null) {
            this.minimumBalance = switch (this.accountType) {
                case SAVINGS -> new BigDecimal("1000.00");
                case CURRENT -> new BigDecimal("10000.00");
                case CHECKING -> new BigDecimal("500.00");
                default -> BigDecimal.ZERO;
            };
        }
    }
}