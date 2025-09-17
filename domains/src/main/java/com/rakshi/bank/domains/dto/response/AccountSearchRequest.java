package com.rakshi.bank.domains.dto.response;

import com.rakshi.bank.domains.enums.AccountStatus;
import com.rakshi.bank.domains.enums.AccountType;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountSearchRequest(
        String userId,
        String accountNumber,
        List<AccountType> accountTypes,
        List<AccountStatus> accountStatuses,
        String accountNameContains,
        String branchCode,

        // Balance filters
        BigDecimal minBalance,
        BigDecimal maxBalance,

        // Date filters
        LocalDate createdAfter,
        LocalDate createdBefore,
        LocalDate lastTransactionAfter,
        LocalDate lastTransactionBefore,

        // Flags
        Boolean isJointAccount,
        Boolean isBusinessAccount,
        Boolean isDormant,
        Boolean allowOnlineTransactions,

        // Pagination
        @Min(value = 0, message = "Page number must be non-negative")
        Integer page,

        @Min(value = 1, message = "Page size must be positive")
        Integer size,

        String sortBy, // accountNumber, balance, createdAt, lastTransactionDate
        String sortDirection // ASC, DESC
) {
}