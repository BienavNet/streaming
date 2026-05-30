package com.api.streaming.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.api.streaming.domain.valueObjects.PaymentId;

public class PaymentUTest {

    private Payment payment;
    
    @BeforeEach
    void setUp() {
        this.payment = new Payment(new PaymentId("234lkjl"), "4242424242424242", 12, 2026, "123", "Banco");
    }

    @Test
    void shouldCreateValidPayment() {
        assertThat(this.payment).isNotNull().isInstanceOf(Payment.class);
    }

    @Test
    void shouldThrowExceptionWhenInvalidUpdate() {
        assertThatThrownBy(() -> this.payment.setNumber("42424242"))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.payment.setNumber(null))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.payment.setMonth(13))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.payment.setCvv("1234"))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldUpdateValues() {
        this.payment.setNumber("4545454545454545");
        assertThat(this.payment.getNumber()).isEqualTo("4545454545454545");

        this.payment.setMonth(10);
        assertThat(this.payment.getMonth()).isEqualTo(10);

        this.payment.setCvv("123");
        assertThat(this.payment.getCvv()).isEqualTo("123");

        this.payment.setNumber("354545454545454");
        assertThat(this.payment.getNumber()).isEqualTo("354545454545454");

        this.payment.setCvv("1234");
        assertThat(this.payment.getCvv()).isEqualTo("1234");
    }
}
