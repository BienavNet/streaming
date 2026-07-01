package com.api.streaming.application.useCases.Order;

import java.util.List;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetOrdersByAccountIdUseCase {
    private final OrderDetailsInterface orderDetailsInterface;

    public List<OrderDetailsQuery> execute(AccountId accountId) {
        return this.orderDetailsInterface.getOrdersDetailsByAccountId(accountId);
    }
}
