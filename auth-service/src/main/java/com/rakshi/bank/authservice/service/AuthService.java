package com.rakshi.bank.authservice.service;

import com.rakshi.bank.domains.dto.request.LoginRequest;
import com.rakshi.bank.domains.dto.response.ApiResponse;

public interface AuthService {

    ApiResponse generateTokenEmail(LoginRequest loginRequest);

    ApiResponse generateTokenPhone(LoginRequest loginRequest);

}
