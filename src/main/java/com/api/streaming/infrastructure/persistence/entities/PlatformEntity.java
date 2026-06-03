package com.api.streaming.infrastructure.persistence.entities;

import jakarta.persistence.Column;
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
@Table (name = "platform")
public class PlatformEntity {

    @Id
    private String id;

    @Column (unique = true)
    private String name;

    private String link;
    private String icon;

    // @OneToMany(
    //     mappedBy = "platform",
    //     cascade = CascadeType.ALL,
    //     fetch = FetchType.LAZY,
    //     orphanRemoval = true
    // )
    // private List<AccountEntity> accounts = new ArrayList<>();
}
