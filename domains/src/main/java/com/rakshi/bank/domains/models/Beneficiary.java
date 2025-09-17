package com.rakshi.bank.domains.models;

import com.rakshi.bank.domains.enums.BeneficiaryType;
import com.rakshi.bank.domains.utils.ConvertBooleanAttributes;
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
@Table(name = "beneficiaries", indexes = {
        @Index(name = "idx_beneficiary_from_account", columnList = "fromAccountId"),
        @Index(name = "idx_beneficiary_to_account", columnList = "toAccountNumber"),
        @Index(name = "idx_beneficiary_type", columnList = "beneficiaryType"),
        @Index(name = "idx_beneficiary_name", columnList = "beneficiaryName"),
        @Index(name = "idx_beneficiary_nickname", columnList = "nickName")
})
public class Beneficiary implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long beneficiaryId;

    @Column(nullable = false)
    private String fromAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccountId", insertable = false, updatable = false)
    private Account fromAccount;

    @Column(nullable = false, length = 100)
    private String beneficiaryName;

    @Column(length = 50)
    private String nickName;

    @Column(nullable = false, length = 20)
    private String toAccountNumber;

    @Column(length = 15)
    private String ifscCode;

    @Column(length = 100)
    private String bankName;

    @Column(length = 100)
    private String branchName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeneficiaryType beneficiaryType;

    @Column(length = 500)
    private String address;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isActive = true;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isVerified = false;

    @Convert(converter = ConvertBooleanAttributes.class)
    @Builder.Default
    private boolean isFavorite = false;

    // Transaction limits for this beneficiary
    @Column(precision = 15, scale = 2)
    private BigDecimal dailyLimit;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyLimit;

    @Column(precision = 15, scale = 2)
    private BigDecimal perTransactionLimit;

    private LocalDateTime lastUsedAt;
    private Integer usageCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    @PrePersist
    private void setDefaults() {
        if (this.usageCount == null) {
            this.usageCount = 0;
        }
        if (this.dailyLimit == null) {
            this.dailyLimit = new BigDecimal("100000.00"); // Default daily limit
        }
        if (this.monthlyLimit == null) {
            this.monthlyLimit = new BigDecimal("1000000.00"); // Default monthly limit
        }
        if (this.perTransactionLimit == null) {
            this.perTransactionLimit = new BigDecimal("50000.00"); // Default per transaction limit
        }
    }
}