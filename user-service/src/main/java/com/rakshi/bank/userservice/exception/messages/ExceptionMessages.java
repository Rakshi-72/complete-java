package com.rakshi.bank.userservice.exception.messages;

public interface ExceptionMessages {

    String USER_ALREADY_EXISTS_BY_PHONE_NUMBER = "User already exists with phone number %s";
    String USER_NOT_FOUND_BY_PHONE_NUMBER = "User not found with phone number %s";
    String USER_NOT_FOUND_BY_ID = "User not found with id %s";
    String USER_ALREADY_EXISTS_BY_EMAIL = "User already exists with email %s";
    String USER_NOT_FOUND_BY_EMAIL = "User not found with email %s";
    String USER_REGISTRATION_FAILED = "User registration failed";

}
