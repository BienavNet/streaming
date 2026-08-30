package com.api.streaming.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.OrderDetailsSnapshot;

public interface OrderDetailsSnapshotJpaRepository extends JpaRepository<OrderDetailsSnapshot, String> {
    
}
