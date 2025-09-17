package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.TransactionStatus;
import com.rakshi.bank.domains.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionSummaryResponse(
        String transactionId,
        String transactionReference,
        TransactionType transactionType,
        BigDecimal amount,
        TransactionStatus status,
        String description,
        String toBeneficiaryName,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime transactionDate
) implements Serializable {
}