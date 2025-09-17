package com.rakshi.bank.authservice.service.impls;

import com.rakshi.bank.authservice.dtos.JwtResponse;
import com.rakshi.bank.authservice.security.jwt.JwtUtil;
import com.rakshi.bank.authservice.service.AuthService;
import com.rakshi.bank.domains.dto.commons.ApiStatus;
import com.rakshi.bank.domains.dto.request.LoginRequest;
import com.rakshi.bank.domains.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse generateTokenEmail(LoginRequest loginRequest) {
        return getToken(loginRequest.email(), loginRequest.password(), loginRequest);
    }

    @Override
    public ApiResponse generateTokenPhone(LoginRequest loginRequest) {
        return this.getToken(loginRequest.phone(), loginRequest.password(), loginRequest);
    }

    private ApiResponse<Object> getToken(String identifier, String password, LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(identifier, password);
        this.authenticationManager.authenticate(authToken);

        String jwt = jwtUtil.generateJwtToken(identifier, loginRequest);
        return ApiResponse
                .builder()
                .success(true)
                .data(new JwtResponse(jwt))
                .message("Authentication successful")
                .status(ApiStatus.SUCCESS)
                .build();
    }

}
