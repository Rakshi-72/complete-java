package com.rakshi.bank.userservice.controller;

import com.rakshi.bank.userservice.service.userService.UserService;
import com.rakshi.bank.userservice.utils.annotations.WrapData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{identity}")
    @WrapData(message = "user found")
    public Object findByIdentity(@PathVariable String identity) {
        return userService.findByIdentity(identity);
    }

}
