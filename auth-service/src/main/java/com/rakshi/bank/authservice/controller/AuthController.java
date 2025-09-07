package com.rakshi.bank.authservice.controller;

import com.rakshi.bank.authservice.service.AuthService;
import com.rakshi.bank.domains.dto.request.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/email")
    public Object loginWithEmail(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.generateTokenEmail(loginRequest);
    }

    @PostMapping("/login/phone")
    public Object loginWithPhone(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.generateTokenPhone(loginRequest);
    }
}