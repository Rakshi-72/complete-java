package com.rakshi.bank.userservice.repository;

import com.rakshi.bank.domains.enums.Roles;
import com.rakshi.bank.domains.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional(readOnly = true)
    List<User> findByActiveTrue();

    @Transactional(readOnly = true)
    List<User> findByVerifiedTrue();

    // Security & Authentication Queries
    @Transactional(readOnly = true)
    List<User> findByFailedLoginAttemptsGreaterThanEqual(Integer threshold);

    @Transactional(readOnly = true)
    List<User> findByLockedTrue();

    @Transactional(readOnly = true)
    List<User> findByTwoFactorEnabledTrue();

    // Fix: Return Optional instead of List for single result
    @Transactional(readOnly = true)
    Optional<User> findByEmailAndActiveTrue(String email);

    // Role-based queries using JOIN
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.role = :role")
    List<User> findByRole(@Param("role") Roles role);

    // Alternative: Find users with specific role (if you prefer method naming)
    @Transactional(readOnly = true)
    List<User> findByRoles_Role(Roles role);

    // Additional useful queries
    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE u.active = true AND u.verified = true")
    List<User> findActiveAndVerifiedUsers();

    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :threshold AND u.locked = false")
    List<User> findUsersEligibleForLocking(@Param("threshold") Integer threshold);

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countActiveUsers();

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(u) FROM User u WHERE u.verified = true")
    long countVerifiedUsers();

    // Find users by multiple roles
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.role IN :roles")
    List<User> findByRolesIn(@Param("roles") List<Roles> roles);

    // Search users by name (case-insensitive)
    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);

    // Find users by location
    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE u.address.city = :city")
    List<User> findByCity(@Param("city") String city);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE u.address.state = :state")
    List<User> findByState(@Param("state") String state);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE u.address.country = :country")
    List<User> findByCountry(@Param("country") String country);
}