package com.rakshi.bank.domains.dto.response;

import com.rakshi.bank.domains.enums.TransactionStatus;
import com.rakshi.bank.domains.enums.TransactionType;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TransactionSearchRequest(
        String accountId,
        List<String> accountIds,
        String transactionReference,
        List<TransactionType> transactionTypes,
        List<TransactionStatus> transactionStatuses,

        // Amount filters
        BigDecimal minAmount,
        BigDecimal maxAmount,

        // Date filters
        LocalDateTime transactionDateFrom,
        LocalDateTime transactionDateTo,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,

        // Text search
        String descriptionContains,
        String particularsContains,
        String merchantNameContains,
        String externalReference,
        String chequeNumber,

        // Transfer filters
        String toAccountNumber,
        String toBeneficiaryName,

        // Location and device
        String transactionLocation,
        String processedBy,
        String initiatedBy,

        // Pagination
        @Min(value = 0, message = "Page number must be non-negative")
        Integer page,

        @Min(value = 1, message = "Page size must be positive")
        Integer size,

        String sortBy, // transactionDate, amount, status, type
        String sortDirection // ASC, DESC
) {
}