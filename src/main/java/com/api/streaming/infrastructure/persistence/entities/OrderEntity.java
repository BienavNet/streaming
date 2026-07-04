package com.api.streaming.infrastructure.persistence.entities;

import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
public class OrderEntity {

    @Id
    private String id;

    @NotNull
    private String account;
    @NotNull
    private Integer profileAmount;
    @NotNull
    private LocalDate buyDate;
    @NotNull
    private LocalDate finishDate;
    @NotNull
    private Double paymentAmount;
    @NotNull
    private String orderType;
}
