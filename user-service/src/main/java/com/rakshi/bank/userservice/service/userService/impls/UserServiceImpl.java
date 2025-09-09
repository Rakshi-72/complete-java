package com.rakshi.bank.userservice.service.userService.impls;

import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.domains.utils.MapperUtil;
import com.rakshi.bank.userservice.exception.exceptions.InvalidIdentity;
import com.rakshi.bank.userservice.exception.exceptions.PermissionNotPassedException;
import com.rakshi.bank.userservice.exception.exceptions.UserNotFoundException;
import com.rakshi.bank.userservice.exception.messages.ExceptionMessages;
import com.rakshi.bank.userservice.repository.UserRepository;
import com.rakshi.bank.userservice.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String PREFIX_CUSTOMER = "CUST";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    @Override
    public UserResponse findByIdentity(String identity) {

        if (identity == null || identity.isBlank()) {
            log.info("Identity is null or blank");
            throw new InvalidIdentity("Identity cannot be null or blank");
        }

        final String trimmed = identity.trim();

        if (trimmed.startsWith(PREFIX_CUSTOMER)) {
            return findByCustomerId(trimmed);
        } else if (EMAIL_PATTERN.matcher(trimmed).matches()) {
            return findByEmail(trimmed);
        } else if (PHONE_NUMBER_PATTERN.matcher(trimmed).matches()) {
            return findByPhoneNumber(trimmed);
        } else {
            log.info("Identity does not match any of the regex");
            throw new InvalidIdentity(ExceptionMessages.INVALID_IDENTITY.formatted(trimmed));
        }

    }

    private UserResponse findByEmail(String email) {

        log.info("Finding user by email {}", email);
        return fetchAndRespond(() -> userRepository.findByEmail(email), ExceptionMessages.USER_NOT_FOUND_BY_EMAIL.formatted(email), User::getEmail);

    }

    private UserResponse findByPhoneNumber(String phoneNumber) {

        log.info("Finding user by phone number {}", phoneNumber);
        return fetchAndRespond(() -> userRepository.findByPhoneNumber(phoneNumber), ExceptionMessages.USER_NOT_FOUND_BY_PHONE_NUMBER.formatted(phoneNumber), User::getPhoneNumber);

    }

    private UserResponse findByCustomerId(String customerId) {
        log.info("Finding user by customer id {}", customerId);
        return fetchAndRespond(() -> userRepository.findById(customerId), ExceptionMessages.USER_NOT_FOUND_BY_ID.formatted(customerId), User::getUserId);
    }

    private UserResponse fetchAndRespond(Supplier<Optional<User>> fetcher, String notFoundMessage, Function<User, String> resolver) {

        User user = fetcher.get().orElseThrow(() -> {
            log.info("{}", notFoundMessage);
            return new UserNotFoundException(notFoundMessage);
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> userRoles = authentication != null ?
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
                : Collections.emptySet();

        log.info("User roles: {}", userRoles.isEmpty() ? "none" : userRoles);

        boolean isAdmin = userRoles.contains("ADMIN");
        boolean isSameUser = authentication != null && authentication
                .getName()
                .equals(user.getEmail());

        if (!(isAdmin || isSameUser)) {
            throw new PermissionNotPassedException(ExceptionMessages.PERMISSION_DENIED);
        }

        String resolved = resolver.apply(user);
        log.info("isAdmin = {}, isSameUser = {}", isAdmin, isSameUser);
        log.info("User found successfully: {}", resolved);

        return mapperUtil.toResponse(user);
    }

}
