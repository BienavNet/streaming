package com.api.streaming.domain.valueObjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class OrderIdUTest {
    @Test
    void shouldCreateValidOrderId() {
        String id = "123456789asdf";
        OrderId orderId = new OrderId(id);

        assertThat(orderId.value()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionForBlankId() {
        assertThatThrownBy(() -> new OrderId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This is a invalid Id for creating this order.");

        assertThatThrownBy(() -> new OrderId("   "))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new OrderId("123 456123789"))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
