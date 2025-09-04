package com.rakshi.bank.domains.dto.response;

import java.io.Serializable;
import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious
) implements Serializable {
}