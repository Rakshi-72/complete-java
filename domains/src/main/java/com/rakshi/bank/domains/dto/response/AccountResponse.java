package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.AccountStatus;
import com.rakshi.bank.domains.enums.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record AccountResponse(
        String accountId,
        String accountNumber,
        String userId,
        AccountType accountType,
        AccountStatus status,
        String accountName,
        BigDecimal currentBalance,
        BigDecimal availableBalance,
        BigDecimal minimumBalance,
        BigDecimal overdraftLimit,
        BigDecimal interestRate,
        String branchCode,
        String ifscCode,
        String currency,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate accountOpeningDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate accountClosingDate,

        boolean isJointAccount,
        boolean isMinorAccount,
        boolean isBusinessAccount,
        boolean isDormant,
        boolean allowOnlineTransactions,
        boolean allowInternationalTransactions,
        boolean allowAtmTransactions,
        boolean allowChequeTransactions,
        BigDecimal dailyTransactionLimit,
        BigDecimal monthlyTransactionLimit,
        Integer maxTransactionsPerDay,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastTransactionDate,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,

        String createdBy,
        String updatedBy,
        Set<AccountHolderResponse> accountHolders
) implements Serializable {
}