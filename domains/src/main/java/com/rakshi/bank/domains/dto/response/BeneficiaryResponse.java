package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.BeneficiaryType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BeneficiaryResponse(
        Long beneficiaryId,
        String fromAccountId,
        String beneficiaryName,
        String nickName,
        String toAccountNumber,
        String ifscCode,
        String bankName,
        String branchName,
        BeneficiaryType beneficiaryType,
        String address,
        String phoneNumber,
        String email,
        boolean isActive,
        boolean isVerified,
        boolean isFavorite,
        BigDecimal dailyLimit,
        BigDecimal monthlyLimit,
        BigDecimal perTransactionLimit,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastUsedAt,

        Integer usageCount,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,

        String createdBy
) implements Serializable {
}