package com.api.streaming.application.useCases.Order;

import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.valueObjects.OrderId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteOrderByIdUseCase {
    private final OrderInterface orderInterface;

    public void execute(OrderId orderId) {
        this.checkIfOrderExists(orderId);
        this.orderInterface.deleteOrder(orderId);
    }

    private void checkIfOrderExists(OrderId orderId) {
        this.orderInterface.getOrderById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Cannot delete an order that does not exist."));
    }
}
