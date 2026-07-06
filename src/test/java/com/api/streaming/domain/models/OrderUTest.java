package com.api.streaming.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;

public class OrderUTest {
    
    private Order order;

    @BeforeEach
    void setup() {
        this.order = new Order(new OrderId("23429-3242"), new AccountId("8823-3242"), 2, LocalDate.now(), LocalDate.of(2026, 12, 31), 12000.00, OrderType.PROFILE);
    }

    @Test
    void shouldCreateValidModel() {
        assertThat(this.order)
            .isNotNull()
            .isInstanceOf(Order.class);
    }

    @Test
    void shouldUpdateBuyDate() {
        LocalDate newDate = LocalDate.of(2026, 10, 1);
        this.order.setBuyDate(newDate);

        assertThat(this.order.getBuyDate())
            .isNotNull()
            .isEqualTo(newDate);
    }

    @Test
    void shouldUpdateFinishDate() {
        LocalDate newDate = LocalDate.of(2027, 10, 1);
        this.order.setFinishDate(newDate);
        assertThat(this.order.getFinishDate())
        .isNotNull()
        .isEqualTo(newDate);
    }

    @Test
    void shouldUpdatePaymentAmount() {
        this.order.setPaymentAmount(10.00);
        assertThat(this.order.getPaymentAmount())
            .isNotNaN()
            .isEqualTo(10.00);
    }

    @Test
    void shouldUpdateProfilesAmount() {
        this.order.setProfileAmount(1);
        assertThat(this.order.getProfileAmount())
        .isNotNull()
        .isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionWhenCreatingModel() {
        assertThatThrownBy(() -> new Order(new OrderId("23429-3242"), new AccountId("8823-3242"), 2, LocalDate.of(2026, 12, 31), LocalDate.now(), -12000.00, OrderType.PROFILE))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingBuyDate() {
        assertThatThrownBy(() -> this.order.setBuyDate(LocalDate.of(2028, 12, 31)))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.order.setBuyDate(null))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingFinishDate() {
        assertThatThrownBy(() -> this.order.setFinishDate(LocalDate.of(2025, 12, 31)))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.order.setFinishDate(null))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPaymentAmount() {
        assertThatThrownBy(() -> this.order.setPaymentAmount(-12.00))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingProfiles() {
        assertThatThrownBy(() -> this.order.setProfileAmount(-1))
        .isInstanceOf(IllegalArgumentException.class);
    }

}
