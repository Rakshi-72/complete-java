package com.rakshi.bank.domains.dto.request;

import com.rakshi.bank.domains.enums.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank(message = "Account ID is required")
        String accountId,

        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
        BigDecimal amount,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Size(max = 100, message = "Particulars must not exceed 100 characters")
        String particulars,

        // For transfer transactions
        String toAccountId,
        String toAccountNumber,
        String toBeneficiaryName,
        String toIfscCode,

        // External references
        @Size(max = 100, message = "External reference must not exceed 100 characters")
        String externalReference,

        @Size(max = 50, message = "Cheque number must not exceed 50 characters")
        String chequeNumber,

        @Size(max = 100, message = "Merchant name must not exceed 100 characters")
        String merchantName,

        @Size(max = 50, message = "Merchant category must not exceed 50 characters")
        String merchantCategory,

        // Processing details
        @Size(max = 50, message = "Transaction location must not exceed 50 characters")
        String transactionLocation,

        @Size(max = 50, message = "Device ID must not exceed 50 characters")
        String deviceId,

        @Size(max = 50, message = "Processed by must not exceed 50 characters")
        String processedBy
) {
}