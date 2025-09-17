package com.rakshi.bank.domains.dto.request;

import com.rakshi.bank.domains.enums.AccountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateAccountRequest(
        @NotBlank(message = "User ID is required")
        String userId,

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotBlank(message = "Account name is required")
        @Size(min = 3, max = 100, message = "Account name must be between 3 and 100 characters")
        String accountName,

        @NotNull(message = "Initial deposit amount is required")
        @DecimalMin(value = "0.00", message = "Initial deposit must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
        BigDecimal initialDepositAmount,

        @NotBlank(message = "Branch code is required")
        @Size(min = 3, max = 10, message = "Branch code must be between 3 and 10 characters")
        String branchCode,

        @Size(max = 15, message = "IFSC code must not exceed 15 characters")
        String ifscCode,

        @DecimalMin(value = "0.00", message = "Overdraft limit must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid overdraft limit format")
        BigDecimal overdraftLimit,

        @DecimalMin(value = "0.00", message = "Daily transaction limit must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid daily limit format")
        BigDecimal dailyTransactionLimit,

        @DecimalMin(value = "0.00", message = "Monthly transaction limit must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid monthly limit format")
        BigDecimal monthlyTransactionLimit,

        @Min(value = 0, message = "Max transactions per day must be non-negative")
        Integer maxTransactionsPerDay,

        Boolean allowOnlineTransactions,
        Boolean allowInternationalTransactions,
        Boolean allowAtmTransactions,
        Boolean allowChequeTransactions,

        // For joint accounts
        Boolean isJointAccount,
        List<@NotBlank String> jointHolderUserIds,

        Boolean isBusinessAccount,
        Boolean isMinorAccount
) {
}