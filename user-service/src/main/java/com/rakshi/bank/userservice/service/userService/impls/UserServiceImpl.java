package com.rakshi.bank.userservice.service.userService.impls;

import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.domains.utils.MapperUtil;
import com.rakshi.bank.userservice.exception.exceptions.InvalidIdentity;
import com.rakshi.bank.userservice.exception.exceptions.PermissionNotPassedException;
import com.rakshi.bank.userservice.exception.exceptions.UserNotFoundException;
import com.rakshi.bank.userservice.exception.messages.ExceptionMessages;
import com.rakshi.bank.userservice.service.userService.UserCacheService;
import com.rakshi.bank.userservice.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String PREFIX_CUSTOMER = "CUST";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String HEADER_USER_ID = "x-user-id";

    // Pre-compiled patterns for better performance
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    private final MapperUtil mapperUtil;
    private final UserCacheService userCacheService;

    @Override
    public UserResponse findByIdentity(String identity) {
        validateIdentity(identity);

        String trimmedIdentity = identity.trim();

        return resolveUserByIdentityType(trimmedIdentity);
    }

    private void validateIdentity(String identity) {
        if (!StringUtils.hasText(identity)) {
            log.warn("Invalid identity provided: null or blank");
            throw new InvalidIdentity("Identity cannot be null or blank");
        }
    }

    private UserResponse resolveUserByIdentityType(String identity) {
        if (identity.startsWith(PREFIX_CUSTOMER)) {
            return findByCustomerId(identity);
        }

        if (EMAIL_PATTERN.matcher(identity).matches()) {
            return findByEmail(identity);
        }

        if (PHONE_NUMBER_PATTERN.matcher(identity).matches()) {
            return findByPhoneNumber(identity);
        }

        log.warn("Identity does not match any valid pattern: {}", identity);
        throw new InvalidIdentity(ExceptionMessages.INVALID_IDENTITY.formatted(identity));
    }

    private UserResponse findByEmail(String email) {
        User user = userCacheService.getUserByEmail(email);
        validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_EMAIL.formatted(email));
        validateUserAccess(user);

        log.debug("User found successfully by email: {}", email);
        return mapperUtil.toResponse(user);
    }

    private UserResponse findByPhoneNumber(String phoneNumber) {
        User user = userCacheService.getUserByPhoneNumber(phoneNumber);
        validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_PHONE_NUMBER.formatted(phoneNumber));
        validateUserAccess(user);

        log.debug("User found successfully by phone number: {}", phoneNumber);
        return mapperUtil.toResponse(user);
    }

    private UserResponse findByCustomerId(String customerId) {
        User user = userCacheService.getUserById(customerId);
        validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_ID.formatted(customerId));
        validateUserAccess(user);

        log.debug("User found successfully by customer ID: {}", customerId);
        return mapperUtil.toResponse(user);
    }

    private void validateUserExists(User user, String notFoundMessage) {
        if (user == null) {
            log.info("User not found: {}", notFoundMessage);
            throw new UserNotFoundException(notFoundMessage);
        }
    }

    private void validateUserAccess(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication context found");
            throw new PermissionNotPassedException(ExceptionMessages.PERMISSION_DENIED);
        }

        if (hasAdminRole(authentication)) {
            log.debug("Admin access granted");
            return;
        }

        if (isSameUser(user, authentication)) {
            log.debug("Same user access granted");
            return;
        }

        log.warn("Permission denied for user access");
        throw new PermissionNotPassedException(ExceptionMessages.PERMISSION_DENIED);
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_ADMIN::equals);
    }

    private boolean isSameUser(User user, Authentication authentication) {
        String principal = authentication.getName();
        log.debug("User ID in authentication context: {} and fetched user id {}", principal, user.getUserId());
        return user.getUserId().equals(principal);
    }
}