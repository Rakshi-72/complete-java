package com.rakshi.bank.userservice.repository;

import com.rakshi.bank.domains.enums.Roles;
import com.rakshi.bank.domains.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Transactional(readOnly = true)
    Optional<Role> findByRole(Roles role);

    @Transactional(readOnly = true)
    Boolean existsByRole(Roles role);

}
