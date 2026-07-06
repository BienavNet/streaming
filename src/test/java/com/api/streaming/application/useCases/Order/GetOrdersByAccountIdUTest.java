package com.api.streaming.application.useCases.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;

@ExtendWith(MockitoExtension.class)
public class GetOrdersByAccountIdUTest {

    @Mock
    private OrderDetailsInterface orderDetailsInterface;

    @Mock
    private OrderDetailsQuery orderDetailsQueryMock; 

    @InjectMocks
    private GetOrdersByAccountIdUseCase useCase;

    private AccountId accountId;

    @BeforeEach
    void setup() {
        this.accountId = new AccountId("uuid-acc-123");
    }

    @Test
    void shouldReturnListOfOrdersForGivenAccountId() {
        // Given
        List<OrderDetailsQuery> expectedOrders = List.of(this.orderDetailsQueryMock);
        when(this.orderDetailsInterface.getOrdersDetailsByAccountId(this.accountId))
            .thenReturn(expectedOrders);

        // When
        List<OrderDetailsQuery> resultList = this.useCase.execute(this.accountId);

        // Then
        assertThat(resultList)
            .isNotEmpty()
            .hasSize(1)
            .containsExactly(this.orderDetailsQueryMock);

        verify(this.orderDetailsInterface, times(1)).getOrdersDetailsByAccountId(this.accountId);
    }

    @Test
    void shouldReturnEmptyListWhenAccountHasNoOrders() {
        // Given
        when(this.orderDetailsInterface.getOrdersDetailsByAccountId(this.accountId))
            .thenReturn(Collections.emptyList());

        // When
        List<OrderDetailsQuery> resultList = this.useCase.execute(this.accountId);

        // Then
        assertThat(resultList)
            .isNotNull()
            .isEmpty();

        verify(this.orderDetailsInterface, times(1)).getOrdersDetailsByAccountId(this.accountId);
    }
}
