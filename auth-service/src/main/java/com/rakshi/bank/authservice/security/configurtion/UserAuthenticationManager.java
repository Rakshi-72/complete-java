package com.rakshi.bank.authservice.security.configurtion;

import com.rakshi.bank.authservice.constants.AppConstants;
import com.rakshi.bank.authservice.exceptions.CredentialsMissMatchException;
import com.rakshi.bank.authservice.service.UserService;
import com.rakshi.bank.domains.enums.Roles;
import com.rakshi.bank.domains.models.Role;
import com.rakshi.bank.domains.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationManager implements AuthenticationManager {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("Authenticating user {}", authentication);

        String principal = authentication.getName(); // could be email or phone
        String password = authentication.getCredentials().toString();

        // Decide dynamically whether it's email or phone number
        User user = userService.findUserByIdentifier(principal);

        boolean passwordMatched = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatched) {
            log.info("Password does not match for user {}", principal);
            throw new CredentialsMissMatchException(AppConstants.CREDENTIALS_NOT_MATCH_EXCEPTION);
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(Role::getRole)
                .map(Roles::toString)
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("User {} authenticated successfully", principal);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }


}
