package com.rakshi.bank.authservice.service.impls;

import com.rakshi.bank.authservice.constants.AppConstants;
import com.rakshi.bank.authservice.exceptions.UserNotFoundException;
import com.rakshi.bank.authservice.repository.UserRepository;
import com.rakshi.bank.authservice.service.UserService;
import com.rakshi.bank.domains.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Cacheable(value = "users", key = "#identifier", unless = "#result == null ")
    public User findUserByIdentifier(String identifier) {

        log.info("finding user by identifier {}", identifier);

        if (identifier.contains("@")) {
            return repository.findByEmail(identifier).orElseThrow(() -> {
                log.info("User with email {} not found", identifier);
                return new UserNotFoundException(
                        String.format(AppConstants.USER_NOT_FOUND_EXCEPTION_EMAIL, identifier)
                );
            });
        } else {
            return repository.findByPhoneNumber(identifier).orElseThrow(() -> {
                log.info("User with phone number {} not found", identifier);
                return new UserNotFoundException(
                        String.format(AppConstants.USER_NOT_FOUND_EXCEPTION_PHONE_NUMBER, identifier)
                );
            });
        }
    }
}
