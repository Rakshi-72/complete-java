package com.rakshi.bank.userservice.repository;

import com.rakshi.bank.domains.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Transactional(readOnly = true)
    boolean existsByPhoneNumber(String input);

    @Transactional(readOnly = true)
    boolean existsByEmail(String input);

    @Transactional(readOnly = true)
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);
}

