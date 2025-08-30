package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.CustomerType;
import com.rakshi.bank.domains.enums.KycStatus;

import java.time.LocalDateTime;

public record UserSummaryResponse(
        String userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        boolean active,
        boolean verified,
        KycStatus kycStatus,
        CustomerType customerType,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
}