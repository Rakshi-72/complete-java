package com.rakshi.bank.domains.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "account_holders", indexes = {
        @Index(name = "idx_account_holder_account", columnList = "accountId"),
        @Index(name = "idx_account_holder_user", columnList = "userId")
})
public class AccountHolder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private boolean isPrimaryHolder;

    @Column(length = 50)
    private String relationship; // PRIMARY, SPOUSE, CHILD, PARENT, PARTNER, etc.

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String createdBy;
}