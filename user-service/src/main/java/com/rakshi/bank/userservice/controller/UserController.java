package com.rakshi.bank.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/hello")
    public String hello(@RequestHeader("x-username") String useString) {
        System.out.println(useString);
        return "Hello World";
    }

}
