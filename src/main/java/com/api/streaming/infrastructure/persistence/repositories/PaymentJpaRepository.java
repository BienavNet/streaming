package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByNumber(String number);

    List<PaymentEntity> findByBankNameContainingIgnoreCase(String bankName);
    List<PaymentEntity> findByNumberContainingIgnoreCase(String number);
}
