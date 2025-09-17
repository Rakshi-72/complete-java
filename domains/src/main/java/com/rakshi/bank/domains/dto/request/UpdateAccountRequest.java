package com.rakshi.bank.domains.dto.request;

import com.rakshi.bank.domains.enums.AccountStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateAccountRequest(
        @Size(min = 3, max = 100, message = "Account name must be between 3 and 100 characters")
        String accountName,

        AccountStatus status,

        @DecimalMin(value = "0.00", message = "Minimum balance must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid minimum balance format")
        BigDecimal minimumBalance,

        @DecimalMin(value = "0.00", message = "Overdraft limit must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid overdraft limit format")
        BigDecimal overdraftLimit,

        @DecimalMin(value = "0.00", message = "Interest rate must be non-negative")
        @Digits(integer = 3, fraction = 2, message = "Invalid interest rate format")
        BigDecimal interestRate,

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
        Boolean allowChequeTransactions
) {
}