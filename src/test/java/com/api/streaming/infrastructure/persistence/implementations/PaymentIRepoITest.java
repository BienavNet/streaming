package com.api.streaming.infrastructure.persistence.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;
import com.api.streaming.infrastructure.persistence.repositories.PaymentJpaRepository;

@DataJpaTest
@Import({PaymentIRepository.class, PaymentMapperImpl.class})
public class PaymentIRepoITest {
    
    @Autowired
    private PaymentIRepository paymentIRepository;

    @Autowired
    private PaymentJpaRepository jpaRepository;

    private PaymentEntity createPaymentEntity(String id, String number) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setNumber(number);
        entity.setMonth(10);
        entity.setYear(2028);
        entity.setCvv("123");
        entity.setBankName("Banco");

        return entity;
    }

    @Test
    void shouldFindPaymentById() {
        PaymentEntity entity = this.createPaymentEntity("uuid-2112", "4242422424244224");
        this.jpaRepository.saveAndFlush(entity);

        PaymentId paymentId = new PaymentId(entity.getId());
        Optional<Payment> result = this.paymentIRepository.getPayment(paymentId);

        assertThat(result)
        .isPresent();

        assertThat(result.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(entity);
    }

    @Test
    void shouldReturnAllPayments() {
        PaymentEntity entity1 = this.createPaymentEntity("uuid-2112", "4242422424244224");
        PaymentEntity entity2 = this.createPaymentEntity("uuid-83223", "4242422424244225");

        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2));


        List<Payment> result = this.paymentIRepository.getPayments();

        assertThat(result)
        .isNotNull()
        .hasSize(2);
    }

    @Test
    void shouldSaveNewPayment() {
        Payment paymentToSave = new Payment(new PaymentId("uuid-2342"), "4242422424244224", 10, 2026, "000", "bank");

        Payment savedPayment = this.paymentIRepository.savePayment(paymentToSave);

        Optional<PaymentEntity> savedInDB = this.jpaRepository.findById(paymentToSave.getId().value());

        assertThat(savedInDB).isPresent();

        assertThat(savedPayment)
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(savedInDB.get());
    }

    @Test
    void shouldDeleteARegisteredPaymentById() {
        PaymentEntity entity = this.createPaymentEntity("uuis-2342", "4242422424244224");

        this.jpaRepository.saveAndFlush(entity);

        this.paymentIRepository.deletePayment(new PaymentId(entity.getId()));

        Optional<PaymentEntity> deletedPayment = this.jpaRepository.findById(entity.getId());

        assertThat(deletedPayment).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenNumberAlreadyExists() {
        PaymentEntity entity = this.createPaymentEntity("uuid-9823", "4242422424244224");
        this.jpaRepository.saveAndFlush(entity);

        Payment paymentToSave = new Payment(new PaymentId("uuid-82883"), "4242422424244224", 10, 2028, "000", "bank");

        assertThatThrownBy(() -> this.paymentIRepository.savePayment(paymentToSave))
        .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldReturnEmptyOrOptionalWhenPaymentDoesNotExist() {
        PaymentId paymentId = new PaymentId("uuid982342");

        assertThat(this.paymentIRepository.getPayment(paymentId))
        .isEmpty();
    }
}
