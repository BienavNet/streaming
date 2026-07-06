package com.api.streaming.application.useCases.Order;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.valueObjects.OrderId;

@ExtendWith(MockitoExtension.class)
public class GetOrderDetailsUTest {

    @Mock
    private OrderDetailsInterface orderDetailsInterface;

    @Mock
    private OrderDetailsQuery orderDetailsQueryMock;

    @InjectMocks
    private GetOrderDetailsByIdUseCase useCase;

    private OrderId orderId;

    @BeforeEach
    void setup() {
        this.orderId = new OrderId("uuid-ord-123");
    }

    @Test
    void shouldReturnOrderDetailsSuccessfully() {
        // Given
        when(this.orderDetailsInterface.getOrderDetailsById(this.orderId))
            .thenReturn(Optional.of(this.orderDetailsQueryMock));

        // When
        OrderDetailsQuery result = this.useCase.execute(this.orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.orderDetailsQueryMock);

        verify(this.orderDetailsInterface, times(1)).getOrderDetailsById(this.orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderDetailsDoNotExist() {
        // Given
        when(this.orderDetailsInterface.getOrderDetailsById(this.orderId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.orderId))
            .isInstanceOf(EntityNotFoundException.class);

        verify(this.orderDetailsInterface, times(1)).getOrderDetailsById(this.orderId);
    }
}
