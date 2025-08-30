package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakshi.bank.domains.enums.CustomerType;
import com.rakshi.bank.domains.enums.Gender;
import com.rakshi.bank.domains.enums.KycStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        String userId,
        String firstName,
        String lastName,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,

        Gender gender,
        Set<RoleResponse> roles,
        String nationality,
        String email,
        String phoneNumber,
        String profileImageUrl,
        boolean active,
        boolean blocked,
        boolean verified,
        boolean deleted,
        boolean locked,
        boolean twoFactorEnabled,
        KycStatus kycStatus,
        CustomerType customerType,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastLoginAt,

        int failedLoginAttempts,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,

        String createdBy,
        String updatedBy,
        AddressResponse address
) {
}