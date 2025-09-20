package com.rakshi.bank.userservice.service.userService.impls;

import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.domains.utils.MapperUtil;
import com.rakshi.bank.userservice.dtos.DeactivateAccountResponse;
import com.rakshi.bank.userservice.exception.exceptions.InvalidIdentity;
import com.rakshi.bank.userservice.exception.exceptions.PermissionNotPassedException;
import com.rakshi.bank.userservice.exception.exceptions.UserNotFoundException;
import com.rakshi.bank.userservice.exception.messages.ExceptionMessages;
import com.rakshi.bank.userservice.security.utils.AuthenticationContextService;
import com.rakshi.bank.userservice.service.userService.UserService;
import com.rakshi.bank.userservice.service.userService.repositoryConnections.UserCacheService;
import com.rakshi.bank.userservice.service.userService.repositoryConnections.UserUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of UserService providing user management operations
 * including user lookup by various identifiers and account management.
 * <p>
 * This service handles:
 * - User lookup by email, phone number, or customer ID
 * - Authentication-based access control
 * - User account deactivation
 * - Security-conscious logging with data masking
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final String PREFIX_CUSTOMER = "CUST";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String MASKED_VALUE = "***";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$"
    );

    private final MapperUtil mapperUtil;
    private final UserCacheService userCacheService;
    private final AuthenticationContextService authenticationContextService;
    private final UserUpdateService userUpdateService;

    @Override
    public UserResponse findByIdentity(String identity) {
        log.info("Starting user lookup by identity: {}", maskSensitiveData(identity));

        validateIdentity(identity);
        String trimmedIdentity = identity.trim();

        UserResponse response = resolveUserByIdentityType(trimmedIdentity);
        log.info("User lookup completed successfully for identity: {}", maskSensitiveData(identity));

        return response;
    }

    @Override
    public UserResponse getCurrentlyLoggedInUser() {
        log.debug("Retrieving currently logged-in user information");

        User user = getCurrentUser();
        UserResponse response = mapperUtil.toResponse(user);

        log.debug("Successfully retrieved current user: {}", user.getUserId());
        return response;
    }

    @Override
    @Transactional
    public DeactivateAccountResponse deActivateCurrentUserAccount() {
        log.info("Starting account deactivation process");

        User currentUser = getCurrentUser();
        String userId = currentUser.getUserId();

        log.info("Processing account deactivation for user: {}", userId);

        if (!currentUser.isActive()) {
            log.warn("Account deactivation attempted for already inactive user: {}", userId);
            return createDeactivateResponse("Account is already deactivated");
        }

        currentUser.setActive(false);
        boolean updateSuccess = userUpdateService.updateUser(currentUser);

        String status = updateSuccess ? "successful" : "failed";
        log.info("Account deactivation for user {}: {}", userId, status);

        return createDeactivateResponse(
                updateSuccess ? "Account deactivated successfully" : "Account deactivation failed"
        );
    }


    private User getCurrentUser() {
        Authentication authentication = authenticationContextService.getAuthenticationToken();
        String userId = authentication.getName();

        log.debug("Retrieving user information for authenticated user: {}", userId);

        User user = userCacheService.getUserById(userId);
        validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_ID.formatted(userId));

        log.debug("Successfully retrieved current user from cache: {}", userId);
        return user;
    }

    private void validateIdentity(String identity) {
        if (!StringUtils.hasText(identity)) {
            log.warn("Identity validation failed: provided identity is null or blank");
            throw new InvalidIdentity("Identity cannot be null or blank");
        }
        log.debug("Identity validation passed for: {}", maskSensitiveData(identity));
    }

    private UserResponse resolveUserByIdentityType(String identity) {
        log.debug("Resolving identity type for: {}", maskSensitiveData(identity));

        if (isCustomerId(identity)) {
            log.debug("Identity classified as Customer ID");
            return findByCustomerId(identity);
        }

        if (isEmail(identity)) {
            log.debug("Identity classified as Email address");
            return findByEmail(identity);
        }

        if (isPhoneNumber(identity)) {
            log.debug("Identity classified as Phone number");
            return findByPhoneNumber(identity);
        }

        log.warn("Identity type resolution failed - unrecognized format: {}",
                maskSensitiveData(identity));
        throw new InvalidIdentity(ExceptionMessages.INVALID_IDENTITY.formatted(identity));
    }

    private UserResponse findByEmail(String email) {
        log.debug("Searching for user by email: {}", maskEmail(email));

        try {
            User user = userCacheService.getUserByEmail(email);
            validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_EMAIL.formatted(email));
            validateUserAccess(user, "email lookup");

            log.info("User found successfully by email: {}", maskEmail(email));
            return mapperUtil.toResponse(user);
        } catch (Exception e) {
            log.error("Email lookup failed for: {} - Error: {}", maskEmail(email), e.getMessage());
            throw e;
        }
    }

    private UserResponse findByPhoneNumber(String phoneNumber) {
        log.debug("Searching for user by phone number: {}", maskPhoneNumber(phoneNumber));

        try {
            User user = userCacheService.getUserByPhoneNumber(phoneNumber);
            validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_PHONE_NUMBER.formatted(phoneNumber));
            validateUserAccess(user, "phone number lookup");

            log.info("User found successfully by phone number: {}", maskPhoneNumber(phoneNumber));
            return mapperUtil.toResponse(user);
        } catch (Exception e) {
            log.error("Phone number lookup failed for: {} - Error: {}",
                    maskPhoneNumber(phoneNumber), e.getMessage());
            throw e;
        }
    }

    private UserResponse findByCustomerId(String customerId) {
        log.debug("Searching for user by customer ID: {}", customerId);

        try {
            User user = userCacheService.getUserById(customerId);
            validateUserExists(user, ExceptionMessages.USER_NOT_FOUND_BY_ID.formatted(customerId));
            validateUserAccess(user, "customer ID lookup");

            log.info("User found successfully by customer ID: {}", customerId);
            return mapperUtil.toResponse(user);
        } catch (Exception e) {
            log.error("Customer ID lookup failed for: {} - Error: {}", customerId, e.getMessage());
            throw e;
        }
    }

    private void validateUserExists(User user, String notFoundMessage) {
        if (user == null) {
            log.warn("User validation failed: {}", notFoundMessage);
            throw new UserNotFoundException(notFoundMessage);
        }
        log.debug("User existence validation passed");
    }

    private void validateUserAccess(User user, String operation) {
        log.debug("Validating user access for operation: {}", operation);

        Authentication authentication = authenticationContextService.getAuthenticationToken();

        if (authentication == null) {
            log.warn("Access validation failed: No authentication context for operation: {}", operation);
            throw new PermissionNotPassedException(ExceptionMessages.PERMISSION_DENIED);
        }

        if (hasAdminRole(authentication)) {
            log.debug("Access granted: Admin privileges for operation: {}", operation);
            return;
        }

        if (isSameUser(user, authentication)) {
            log.debug("Access granted: Same user access for operation: {}", operation);
            return;
        }

        log.warn("Access denied: Insufficient permissions for operation: {} - Target user: {}, Requesting user: {}",
                operation, user.getUserId(), authentication.getName());
        throw new PermissionNotPassedException(ExceptionMessages.PERMISSION_DENIED);
    }

    private boolean hasAdminRole(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_ADMIN::equals);

        log.debug("Admin role verification result: {}", isAdmin);
        return isAdmin;
    }

    private boolean isSameUser(User user, Authentication authentication) {
        String requestingUserId = authentication.getName();
        String targetUserId = user.getUserId();
        boolean isSameUser = targetUserId.equals(requestingUserId);

        log.debug("Same user verification - Target: {}, Requesting: {}, Result: {}",
                targetUserId, requestingUserId, isSameUser);
        return isSameUser;
    }

    private boolean isCustomerId(String identity) {
        return Optional.ofNullable(identity)
                .map(id -> id.startsWith(PREFIX_CUSTOMER))
                .orElse(false);
    }

    private boolean isEmail(String identity) {
        return Optional.ofNullable(identity)
                .map(EMAIL_PATTERN::matcher)
                .map(Matcher::matches)
                .orElse(false);
    }

    private boolean isPhoneNumber(String identity) {
        return Optional.ofNullable(identity)
                .map(PHONE_PATTERN::matcher)
                .map(Matcher::matches)
                .orElse(false);
    }

    private DeactivateAccountResponse createDeactivateResponse(String message) {
        return DeactivateAccountResponse.builder()
                .success(message)
                .build();
    }

    private String maskSensitiveData(String data) {
        if (data == null || data.length() <= 4) {
            return MASKED_VALUE;
        }
        return data.substring(0, 2) + MASKED_VALUE + data.substring(data.length() - 2);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***@***.***";
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return "***@***.***";
        }

        return maskSensitiveData(parts[0]) + "@" + maskSensitiveData(parts[1]);
    }

    private String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() <= 6) {
            return "***-***-***";
        }
        return phone.substring(0, 3) + MASKED_VALUE + phone.substring(phone.length() - 3);
    }
}