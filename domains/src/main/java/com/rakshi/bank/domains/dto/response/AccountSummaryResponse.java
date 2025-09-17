package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.AccountStatus;
import com.rakshi.bank.domains.enums.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountSummaryResponse(
        String accountId,
        String accountNumber,
        AccountType accountType,
        AccountStatus status,
        String accountName,
        BigDecimal currentBalance,
        BigDecimal availableBalance,
        String currency,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate accountOpeningDate,

        boolean isJointAccount,
        boolean isBusinessAccount,
        String branchCode
) implements Serializable {
}