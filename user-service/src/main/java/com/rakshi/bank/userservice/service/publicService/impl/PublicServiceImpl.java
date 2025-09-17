package com.rakshi.bank.userservice.service.publicService.impl;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.domains.enums.Roles;
import com.rakshi.bank.domains.models.Role;
import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.domains.utils.MapperUtil;
import com.rakshi.bank.userservice.exception.exceptions.RoleNotFoundException;
import com.rakshi.bank.userservice.exception.exceptions.UserRegistrationException;
import com.rakshi.bank.userservice.exception.messages.ExceptionMessages;
import com.rakshi.bank.userservice.repository.RoleRepository;
import com.rakshi.bank.userservice.repository.UserRepository;
import com.rakshi.bank.userservice.service.publicService.PublicService;
import com.rakshi.bank.userservice.service.validations.ValidationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponse registerUser(CreateUserRequest userRequest) {
        validateUser(userRequest);

        log.info("Registering user {}", userRequest.email());

        User user = mapperUtil.fromRequest(userRequest);
        assignDefaultRole(user);

        log.info("after assigning default role {}", user.getRoles());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = Optional.of(userRepository.save(user)).orElseThrow(() -> new UserRegistrationException(ExceptionMessages.USER_REGISTRATION_FAILED));

        log.info("User registered successfully {}", user.getEmail());

        return mapperUtil.toResponse(savedUser);
    }

    private void validateUser(CreateUserRequest userRequest) {
        log.info("Validating user request {}", userRequest.email());
        validationStrategies.forEach(strategy -> strategy.validate(userRequest));
        log.info("Validation successful for user request {}", userRequest.email());
    }

    private void assignDefaultRole(User user) {
        Roles roles = Roles.CUSTOMER;
        Role role = roleRepository.findByRole(roles).orElseThrow(
                () -> {
                    log.info("roles not found {}", roles);
                    return new RoleNotFoundException(String.format(
                            ExceptionMessages.ROLE_NOT_FOUND, roles
                    ));
                }

        );
        log.info("Assigning role {} to user {}", role.getRole(), user.getEmail());
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);

        log.info("Assigned role {} to user {}", role.getRole(), user.getEmail());
    }
}
