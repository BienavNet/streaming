package com.api.streaming.domain.valueObjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class PaymentIdUTest {
    @Test
    void shouldCreateValidPaymentId() {
        String id = "123456789asdf";
        PaymentId PaymentId = new PaymentId(id);

        assertThat(PaymentId.value()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionForBlankId() {
        assertThatThrownBy(() -> new PaymentId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This is a invalid Id for creating this payment.");

        assertThatThrownBy(() -> new PaymentId("   "))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new PaymentId("123 456123789"))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
