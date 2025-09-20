package com.rakshi.bank.authservice.service;

import com.rakshi.bank.authservice.dtos.JwtResponse;
import com.rakshi.bank.domains.dto.request.LoginRequest;
import com.rakshi.bank.domains.dto.response.ApiResponse;

public interface AuthService {

    ApiResponse<JwtResponse> generateTokenEmail(LoginRequest loginRequest);

    ApiResponse<JwtResponse> generateTokenPhone(LoginRequest loginRequest);

}
