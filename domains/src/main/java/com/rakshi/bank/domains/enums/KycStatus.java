package com.rakshi.bank.domains.enums;

import java.io.Serializable;

public enum KycStatus implements Serializable {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    NOT_INITIALIZED
}
