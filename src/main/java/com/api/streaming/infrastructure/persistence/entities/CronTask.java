package com.api.streaming.infrastructure.persistence.entities;

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
@Table(name = "crontask")
public class CronTask {
    @Id 
    private String id;
    @Nonnull 
    private String name;
    @Nonnull 
    private String description;
    @Nonnull 
    private boolean active;
}
