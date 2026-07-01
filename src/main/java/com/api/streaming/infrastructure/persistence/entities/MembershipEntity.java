package com.api.streaming.infrastructure.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "membership")
public class MembershipEntity {
    @Id
    private String id;
    @NotNull
    private String account;
    @NotNull
    private String membershipConfig;
}
