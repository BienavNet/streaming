package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByAccount(String account);
    List<OrderEntity> findByAccountAndOrderType(String id, String orderType);
}
