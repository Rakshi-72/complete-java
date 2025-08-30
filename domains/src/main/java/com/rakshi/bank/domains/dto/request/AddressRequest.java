package com.rakshi.bank.domains.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Street is required")
        @Size(min = 5, max = 200, message = "Street must be between 5 and 200 characters")
        String street,

        @NotBlank(message = "City is required")
        @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
        String city,

        @NotBlank(message = "State is required")
        @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
        String state,

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
        String country,

        @NotBlank(message = "Pin code is required")
        @Pattern(regexp = "^[0-9]{4,10}$", message = "Pin code must be between 4 and 10 digits")
        String pinCode
) {
}