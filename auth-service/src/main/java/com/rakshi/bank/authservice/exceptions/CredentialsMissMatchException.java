package com.rakshi.bank.authservice.exceptions;

public class CredentialsMissMatchException extends RuntimeException {
    public CredentialsMissMatchException(String message) {
        super(message);
    }
}
