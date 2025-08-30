package com.rakshi.bank.domains.dto.request;

import com.rakshi.bank.domains.enums.CustomerType;
import com.rakshi.bank.domains.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters and spaces")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters and spaces")
        String lastName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        String password,

        @NotBlank(message = "Nationality is required")
        @Size(min = 2, max = 50, message = "Nationality must be between 2 and 50 characters")
        String nationality,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        String phoneNumber,

        @URL(message = "Profile image URL must be valid")
        String profileImageUrl,

        @NotNull(message = "Customer type is required")
        CustomerType customerType,

        @Valid
        @NotNull(message = "Address is required")
        AddressRequest address
) {
}