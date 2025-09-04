package com.rakshi.bank.domains.dto.response;

import java.io.Serializable;

public record AddressResponse(
        String street,
        String city,
        String state,
        String country,
        String pinCode
) implements Serializable {
}