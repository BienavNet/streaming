package com.api.streaming.infrastructure.persistence.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (name = "payment")
public class PaymentEntity {
    @Id
    private String id;

    @Nonnull
    @Column(unique = true)
    private String number;

    @Nonnull
    @Column(name = "`month`")
    private Integer month;

    @Nonnull
    @Column(name = "`year`")
    private Integer year;
    
    @Nonnull
    private String cvv;

    private String bankName;

    // @OneToOne(
    //     fetch = FetchType.LAZY
    // )
    // @JoinColumn(
    //     name = "account_id"
    // )
    // private AccountEntity account;
}
