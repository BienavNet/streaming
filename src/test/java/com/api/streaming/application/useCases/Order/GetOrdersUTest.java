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
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

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
        Page<OrderDetailsQuery> mock = new Page<>(expectedOrders, 2L, 1);
        Pageable pageable = new Pageable(0, 10);

        when(this.orderDetailsInterface.getOrdersDetails(pageable))
            .thenReturn(mock);

        // When
        Page<OrderDetailsQuery> resultList = this.useCase.execute(pageable);

        // Then
        assertThat(resultList.content())
            .isNotEmpty()
            .hasSize(1)
            .containsExactly(this.orderDetailsQueryMock);

        verify(this.orderDetailsInterface, times(1)).getOrdersDetails(pageable);
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersExistInSystem() {

        List<OrderDetailsQuery> expectedOrders = Collections.emptyList();
        Page<OrderDetailsQuery> mock = new Page<>(expectedOrders, 2L, 1);
        Pageable pageable = new Pageable(0, 10);

        // Given
        when(this.orderDetailsInterface.getOrdersDetails(pageable))
            .thenReturn(mock);

        // When
        Page<OrderDetailsQuery> resultList = this.useCase.execute(pageable);

        // Then
        assertThat(resultList.content())
            .isNotNull()
            .isEmpty();

        verify(this.orderDetailsInterface, times(1)).getOrdersDetails(pageable);
    }
}

