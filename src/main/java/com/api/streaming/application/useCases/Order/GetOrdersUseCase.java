package com.api.streaming.application.useCases.Order;

import java.util.List;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetOrdersUseCase {
    private final OrderDetailsInterface orderDetailsInterface;

    public List<OrderDetailsQuery> execute() {
        return this.orderDetailsInterface.getOrdersDetails();
    }
}
