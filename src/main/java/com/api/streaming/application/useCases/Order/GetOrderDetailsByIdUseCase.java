package com.api.streaming.application.useCases.Order;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.valueObjects.OrderId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetOrderDetailsByIdUseCase {
    private final OrderDetailsInterface orderDetailsInterface;

    public OrderDetailsQuery execute(OrderId orderId) {
        return this.orderDetailsInterface.getOrderDetailsById(orderId)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
