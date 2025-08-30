package com.rakshi.bank.domains.dto.request;

import com.rakshi.bank.domains.enums.CustomerType;
import com.rakshi.bank.domains.enums.Gender;
import com.rakshi.bank.domains.enums.KycStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public record UpdateUserRequest(
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters and spaces")
        String firstName,

        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters and spaces")
        String lastName,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        Gender gender,

        @Size(min = 2, max = 50, message = "Nationality must be between 2 and 50 characters")
        String nationality,

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        String phoneNumber,

        @URL(message = "Profile image URL must be valid")
        String profileImageUrl,

        Boolean active,

        Boolean blocked,

        Boolean verified,

        Boolean locked,

        Boolean twoFactorEnabled,

        KycStatus kycStatus,

        CustomerType customerType,

        @Valid
        AddressRequest address
) {
}