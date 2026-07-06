package com.api.streaming.application.useCases.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;

@ExtendWith(MockitoExtension.class)
public class GetOrdersUTest {

    @Mock
    private OrderDetailsInterface orderDetailsInterface;

    @Mock
    private OrderDetailsQuery orderDetailsQueryMock; 

    @InjectMocks
    private GetOrdersUseCase useCase;

    @Test
    void shouldReturnAllOrdersSuccessfully() {
        // Given
        List<OrderDetailsQuery> expectedOrders = List.of(this.orderDetailsQueryMock);
        when(this.orderDetailsInterface.getOrdersDetails())
            .thenReturn(expectedOrders);

        // When
        List<OrderDetailsQuery> resultList = this.useCase.execute();

        // Then
        assertThat(resultList)
            .isNotEmpty()
            .hasSize(1)
            .containsExactly(this.orderDetailsQueryMock);

        verify(this.orderDetailsInterface, times(1)).getOrdersDetails();
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersExistInSystem() {
        // Given
        when(this.orderDetailsInterface.getOrdersDetails())
            .thenReturn(Collections.emptyList());

        // When
        List<OrderDetailsQuery> resultList = this.useCase.execute();

        // Then
        assertThat(resultList)
            .isNotNull()
            .isEmpty();

        verify(this.orderDetailsInterface, times(1)).getOrdersDetails();
    }
}

