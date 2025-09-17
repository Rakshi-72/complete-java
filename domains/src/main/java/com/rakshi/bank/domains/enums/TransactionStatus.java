package com.rakshi.bank.domains.enums;

import java.io.Serializable;

public enum TransactionStatus implements Serializable {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED,
    REVERSED,
    TIMEOUT,
    DECLINED,
    ON_HOLD
}