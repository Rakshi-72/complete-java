package com.rakshi.bank.domains.dto.response;

import java.io.Serializable;

public record ValidationError(
        String field,
        String message,
        Object rejectedValue
) implements Serializable {
}