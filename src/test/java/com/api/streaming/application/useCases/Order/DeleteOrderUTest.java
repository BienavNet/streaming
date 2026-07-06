package com.api.streaming.application.useCases.Order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.OrderId;

@ExtendWith(MockitoExtension.class)
public class DeleteOrderUTest {

    @Mock
    private OrderInterface orderInterface;

    @Mock
    private Order orderMock; 

    @InjectMocks
    private DeleteOrderByIdUseCase useCase;

    private OrderId orderId;

    @BeforeEach
    void setup() {
        this.orderId = new OrderId("uuid-ord-123");
    }

    @Test
    void shouldDeleteOrderSuccessfully() {
        // Given
        when(this.orderInterface.getOrderById(this.orderId))
            .thenReturn(Optional.of(this.orderMock));

        // When
        this.useCase.execute(this.orderId);

        // Then
        verify(this.orderInterface, times(1)).getOrderById(this.orderId);
        verify(this.orderInterface, times(1)).deleteOrder(this.orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExist() {
        // Given
        when(this.orderInterface.getOrderById(this.orderId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.orderId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Cannot delete an order that does not exist.");

        // Verificaciones de seguridad de QA
        verify(this.orderInterface, times(1)).getOrderById(this.orderId);
        verify(this.orderInterface, never()).deleteOrder(any());
    }
}

