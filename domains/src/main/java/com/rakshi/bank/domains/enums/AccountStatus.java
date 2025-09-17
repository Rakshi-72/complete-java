package com.rakshi.bank.domains.enums;

import java.io.Serializable;

public enum AccountStatus implements Serializable {
    ACTIVE,
    INACTIVE,
    BLOCKED,
    FROZEN,
    CLOSED,
    SUSPENDED,
    PENDING_ACTIVATION,
    PENDING_CLOSURE,
    DORMANT,
    RESTRICTED
}