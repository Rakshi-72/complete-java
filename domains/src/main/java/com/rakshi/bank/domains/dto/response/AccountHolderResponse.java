package com.rakshi.bank.domains.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record AccountHolderResponse(
        Long id,
        String accountId,
        String userId,
        boolean isPrimaryHolder,
        String relationship,
        UserSummaryResponse user,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        String createdBy
) implements Serializable {
}