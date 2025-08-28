package com.rakshi.bank.authservice.repository;


import com.rakshi.bank.domains.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
