package com.rakshi.bank.userservice.service.publicService.impl;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.domains.utils.MapperUtil;
import com.rakshi.bank.userservice.exception.exceptions.UserRegistrationException;
import com.rakshi.bank.userservice.exception.messages.ExceptionMessages;
import com.rakshi.bank.userservice.repository.UserRepository;
import com.rakshi.bank.userservice.service.publicService.PublicService;
import com.rakshi.bank.userservice.service.validations.ValidationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicServiceImpl implements PublicService {

    private final UserRepository userRepository;
    private final List<ValidationStrategy> validationStrategies;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(CreateUserRequest userRequest) {
        validateUser(userRequest);

        log.info("Registering user {}", userRequest);

        User user = mapperUtil.fromRequest(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = Optional.of(userRepository.save(user))
                .orElseThrow(() -> new UserRegistrationException(ExceptionMessages.USER_REGISTRATION_FAILED));

        log.info("User registered successfully {}", savedUser);

        return mapperUtil.toResponse(savedUser);
    }

    private void validateUser(CreateUserRequest userRequest) {
        log.info("Validating user request {}", userRequest);
        validationStrategies.forEach(strategy -> strategy.validate(userRequest));
        log.info("Validation successful for user request {}", userRequest);
    }
}
