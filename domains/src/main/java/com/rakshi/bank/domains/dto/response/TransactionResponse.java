package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.TransactionStatus;
import com.rakshi.bank.domains.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        String transactionReference,
        String accountId,
        TransactionType transactionType,
        BigDecimal amount,
        TransactionStatus status,
        String description,
        String particulars,
        BigDecimal balanceAfterTransaction,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime transactionDate,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime valueDate,

        // Transfer details
        String toAccountId,
        String toAccountNumber,
        String toBeneficiaryName,
        String toIfscCode,

        // External references
        String externalReference,
        String chequeNumber,
        String merchantName,
        String merchantCategory,

        // Transaction fees
        BigDecimal transactionFee,
        BigDecimal gstOnFee,

        // Location and device info
        String transactionLocation,
        String deviceId,
        String processedBy,
        String processingBranch,

        String failureReason,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime processedAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime settledAt,

        String initiatedBy,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) implements Serializable {
}