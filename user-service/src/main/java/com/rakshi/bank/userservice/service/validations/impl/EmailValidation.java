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
public class EmailValidation implements ValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public boolean validate(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            log.info("User with email {} already exists", request.email());
            throw new UserAlreadyExistsException(
                    String.format(ExceptionMessages.USER_ALREADY_EXISTS_BY_EMAIL, request.email())
            );
        }
        log.info("User with email {} does not exist", request.email());
        return true;
    }
}
