package com.rakshi.bank.userservice.service.validations.impl;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.userservice.exception.exceptions.UserAlreadyExistsException;
import com.rakshi.bank.userservice.exception.messages.ExceptionMessages;
import com.rakshi.bank.userservice.repository.UserRepository;
import com.rakshi.bank.userservice.service.validations.ValidationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PhoneNumberValidation implements ValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public boolean validate(CreateUserRequest request) {
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            log.info("User with phone number {} already exists", request.phoneNumber());
            throw new UserAlreadyExistsException(
                    String.format(ExceptionMessages.USER_ALREADY_EXISTS_BY_PHONE_NUMBER, request.phoneNumber())
            );
        }
        log.info("User with phone number {} does not exist", request.phoneNumber());
        return true;
    }
}
