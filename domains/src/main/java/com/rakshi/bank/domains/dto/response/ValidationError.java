package com.rakshi.bank.domains.dto.response;

public record ValidationError(
        String field,
        String message,
        Object rejectedValue
) {
}