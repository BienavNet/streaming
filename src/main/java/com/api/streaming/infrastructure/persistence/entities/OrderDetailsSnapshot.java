package com.api.streaming.infrastructure.persistence.entities;

import java.time.LocalDate;


import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "order_details_snapshot")
public class OrderDetailsSnapshot {

    @Id
    private String id;
    @Nonnull
    private String accountEmail;
    @Nonnull
    private String platformName;
    @Nonnull
    private String planName;
    @Nonnull
    private Integer profileAmount;
    @Nonnull
    private LocalDate buyDate;
    @Nonnull
    private LocalDate finishDate;
    @Nonnull
    private Double paymentAmount;
    @Nonnull
    private String orderType;
}
