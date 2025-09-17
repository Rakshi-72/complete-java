package com.rakshi.bank.domains.enums;

import java.io.Serializable;

public enum BeneficiaryType implements Serializable {
    INTERNAL,    // Within same bank
    EXTERNAL,    // Other banks
    INTERNATIONAL,
    UTILITY,     // Bill payments
    MERCHANT,    // E-commerce/retail
    GOVERNMENT   // Tax payments, etc.
}