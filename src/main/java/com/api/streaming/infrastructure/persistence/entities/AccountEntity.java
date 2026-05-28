package com.api.streaming.infrastructure.persistence.entities;


import java.time.LocalDate;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "account")
public class AccountEntity {
    @Id
    private String id;
    @Nonnull
    private String email;
    @Nonnull
    private String password;
    @Nonnull
    private boolean isFreeTrial;
    @Nonnull
    private LocalDate membershipBuyDate;
    @Nonnull
    private LocalDate membershipExpireDate;
    @Nonnull
    private LocalDate registeredAt;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn (name = "platform_id", nullable = false)
    private PlatformEntity platform;

    @OneToOne(
        mappedBy = "account",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @JoinColumn (name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Nonnull
    private String status;
}
