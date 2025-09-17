package com.rakshi.bank.userservice.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Component
@RequiredArgsConstructor
@Slf4j
public class StartUps implements CommandLineRunner {

    private final PasswordEncoder encoder;

    //    you can add any startup logic here
    @Override
    public void run(String... args) throws Exception {
//        encoder.
    }
}
