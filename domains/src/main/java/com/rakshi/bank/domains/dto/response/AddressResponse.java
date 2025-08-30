package com.rakshi.bank.domains.dto.response;

public record AddressResponse(
        String street,
        String city,
        String state,
        String country,
        String pinCode
) {
}