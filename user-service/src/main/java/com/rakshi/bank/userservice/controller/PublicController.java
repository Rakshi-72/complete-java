package com.rakshi.bank.userservice.controller;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.userservice.service.publicService.PublicService;
import com.rakshi.bank.userservice.utils.annotations.WrapData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
public class PublicController {

    private final PublicService publicService;

    @PostMapping("/register")
    @WrapData(status = HttpStatus.CREATED, message = "User registered successfully")
    public Object register(@Valid @RequestBody CreateUserRequest request) {
        return publicService.registerUser(request);
    }

}
