package com.rakshi.bank.domains.dto.commons;

import java.io.Serializable;

public enum ApiStatus implements Serializable {
    SUCCESS,
    FAILURE,
    ERROR,
    UNAUTHORIZED,
    NOT_FOUND,
    BAD_REQUEST,
    CONFLICT,
    INTERNAL_SERVER_ERROR,
    SERVICE_UNAVAILABLE,
    NOT_IMPLEMENTED,
}
