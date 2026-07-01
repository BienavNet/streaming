package com.api.streaming.infrastructure.persistence.entities;

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
@Table(name = "membershipConfig")
public class MembershipConfigEntity {

    @Id
    private String id;
    @NotNull
    private String platform;
    @NotNull
    private String plan;
    @NotNull
    private int profiles;
}
