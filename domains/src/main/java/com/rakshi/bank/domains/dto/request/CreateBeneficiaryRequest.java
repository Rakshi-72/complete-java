package com.rakshi.bank.domains.dto.request;

import com.rakshi.bank.domains.enums.BeneficiaryType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateBeneficiaryRequest(
        @NotBlank(message = "From account ID is required")
        String fromAccountId,

        @NotBlank(message = "Beneficiary name is required")
        @Size(min = 2, max = 100, message = "Beneficiary name must be between 2 and 100 characters")
        String beneficiaryName,

        @Size(max = 50, message = "Nick name must not exceed 50 characters")
        String nickName,

        @NotBlank(message = "To account number is required")
        @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
        String toAccountNumber,

        @Size(min = 11, max = 15, message = "IFSC code must be between 11 and 15 characters")
        String ifscCode,

        @Size(max = 100, message = "Bank name must not exceed 100 characters")
        String bankName,

        @Size(max = 100, message = "Branch name must not exceed 100 characters")
        String branchName,

        @NotNull(message = "Beneficiary type is required")
        BeneficiaryType beneficiaryType,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        String phoneNumber,

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @DecimalMin(value = "0.01", message = "Daily limit must be positive")
        @Digits(integer = 13, fraction = 2, message = "Invalid daily limit format")
        BigDecimal dailyLimit,

        @DecimalMin(value = "0.01", message = "Monthly limit must be positive")
        @Digits(integer = 13, fraction = 2, message = "Invalid monthly limit format")
        BigDecimal monthlyLimit,

        @DecimalMin(value = "0.01", message = "Per transaction limit must be positive")
        @Digits(integer = 13, fraction = 2, message = "Invalid per transaction limit format")
        BigDecimal perTransactionLimit,

        Boolean isFavorite
) {
}