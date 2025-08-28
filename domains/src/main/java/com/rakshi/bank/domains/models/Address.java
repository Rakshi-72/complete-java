package com.rakshi.bank.domains.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;
}

