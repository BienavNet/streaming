package com.api.streaming.infrastructure.persistence.entities;


import java.time.LocalDate;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
    private LocalDate registeredAt;
    
    @Nonnull
    @Column(name = "platform_id")
    private String platform;
    @NonNull
     @Column(name = "payment_id")
    private String payment;
}
