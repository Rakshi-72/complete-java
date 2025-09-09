package com.rakshi.bank.userservice.service.userService;

import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCacheService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#email")
    public User getUserByEmail(String email) {
        log.info("Fetching user from DB by email {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    @Cacheable(value = "users", key = "#phoneNumber")
    public User getUserByPhoneNumber(String phoneNumber) {
        log.info("Fetching user from DB by phone {}", phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @Cacheable(value = "users", key = "#customerId")
    public User getUserById(String customerId) {
        log.info("Fetching user from DB by id {}", customerId);
        return userRepository.findById(customerId).orElse(null);
    }
}
