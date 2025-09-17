package com.rakshi.bank.domains.models;

import com.rakshi.bank.domains.enums.CustomerType;
import com.rakshi.bank.domains.enums.Gender;
import com.rakshi.bank.domains.enums.KycStatus;
import com.rakshi.bank.domains.utils.ConvertBooleanAttributes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_phone", columnList = "phoneNumber", unique = true)
        }
)
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "cust-id-generator")
    @GenericGenerator(
            name = "cust-id-generator",
            strategy = "com.rakshi.bank.domains.utils.CustomIdGenerator"
    )
    private String userId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

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
    private KycStatus kycStatus = KycStatus.NOT_INITIALIZED;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType = CustomerType.INDIVIDUAL;

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
}
