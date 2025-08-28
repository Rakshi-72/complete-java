package com.rakshi.bank.domains.models;

import com.rakshi.bank.domains.enums.CustomerType;
import com.rakshi.bank.domains.enums.Gender;
import com.rakshi.bank.domains.enums.KycStatus;
import com.rakshi.bank.domains.utils.ConvertBooleanAttributes;
import com.rakshi.bank.domains.utils.CustomIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "users", indexes = {})
public class User {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private String nationality;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;

    @Convert(converter = ConvertBooleanAttributes.class)
    private boolean active;

    @Convert(converter = ConvertBooleanAttributes.class)
    private boolean blocked;

    @Convert(converter = ConvertBooleanAttributes.class)
    private boolean verified;

    @Convert(converter = ConvertBooleanAttributes.class)
    private boolean deleted;

    @Convert(converter = ConvertBooleanAttributes.class)
    private boolean locked;

    @Convert(converter = ConvertBooleanAttributes.class)
    private boolean twoFactorEnabled;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    private LocalDateTime lastLoginAt;
    private int failedLoginAttempts;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    @Embedded
    private Address address;


    @PrePersist
    private void generateUserId() {
        if (this.userId == null)
            userId = String.format("CUST%08d", CustomIdGenerator.USER_ID_GENERATOR.incrementAndGet());
        this.kycStatus = KycStatus.NOT_INITIALIZED;
        this.verified = false;
        this.blocked = false;
        this.locked = false;
        this.active = true;
        this.deleted = false;
        this.failedLoginAttempts = 0;
        this.twoFactorEnabled = false;

    }

}
