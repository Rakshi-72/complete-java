package com.rakshi.bank.domains.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

        @Email(message = "Invalid email format")
        String email,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be 10 digits")
        String phone,

        @NotBlank(message = "Password must not be blank")
        String password
) {
}