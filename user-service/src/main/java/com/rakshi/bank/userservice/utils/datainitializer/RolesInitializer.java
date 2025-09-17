package com.rakshi.bank.userservice.utils.datainitializer;

import com.rakshi.bank.domains.enums.Roles;
import com.rakshi.bank.domains.models.Role;
import com.rakshi.bank.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.Arrays;

//@Component
@RequiredArgsConstructor
@Slf4j
public class RolesInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(Roles.values())
                .filter(role -> !roleRepository.existsByRole(role))
                .map(roles -> Role.builder().role(roles).build())
                .peek(roles -> log.info("Initializing role {}", roles))
                .forEach(roleRepository::save);
    }
}
