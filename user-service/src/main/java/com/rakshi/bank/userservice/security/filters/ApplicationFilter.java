package com.rakshi.bank.userservice.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("x-user-id");
        String roles = request.getHeader("x-roles");

        log.info("userId {} roles {}", userId, roles);

        List<SimpleGrantedAuthority> roleSet = Collections.emptyList();
        if (!StringUtils.isBlank(roles)) {
            roleSet = Arrays.stream(roles.split("\\|")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userId, null, roleSet
                )
        );

        filterChain.doFilter(request, response);

    }
}
