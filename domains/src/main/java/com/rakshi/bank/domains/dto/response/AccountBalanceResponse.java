package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountBalanceResponse(
        String accountId,
        String accountNumber,
        BigDecimal currentBalance,
        BigDecimal availableBalance,
        BigDecimal minimumBalance,
        BigDecimal overdraftLimit,
        String currency,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastTransactionDate,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime balanceAsOf
) implements Serializable {
}