package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.infrastructure.mappers.PaymentMapper;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;
import com.api.streaming.infrastructure.persistence.repositories.PaymentJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentIRepository implements PaymentInterface {

    private final PaymentJpaRepository jpa;
    private final PaymentMapper mapper;

    @Override
    public Optional<Payment> getPayment(PaymentId paymentId) {
        return this.jpa.findById(paymentId.value())
            .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public List<Payment> getPayments() {
        List<PaymentEntity> entities = this.jpa.findAll();
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public Payment savePayment(Payment payment) {
        PaymentEntity entityToSave = this.mapper.fromDomainToEntity(payment);
        PaymentEntity savedEntity = this.jpa.saveAndFlush(entityToSave);
        return this.mapper.fromEntityToDomain(savedEntity);
    }

    @Override
    public void deletePayment(PaymentId paymentId) {
        this.jpa.deleteById(paymentId.value());
    }

    @Override
    public Optional<Payment> getPaymentByNumber(String number) {
        return this.jpa.findByNumber(number)
            .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public List<Payment> findByBankName(String name) {
        return this.jpa.findByBankNameContainingIgnoreCase(name)
                .stream()
                .map(this.mapper::fromEntityToDomain)
                .toList();
    }

    @Override
    public List<Payment> findByNumber(String number) {
        return this.jpa.findByNumberContainingIgnoreCase(number)
                .stream()
                .map(this.mapper::fromEntityToDomain)
                .toList();
    }
    
}
