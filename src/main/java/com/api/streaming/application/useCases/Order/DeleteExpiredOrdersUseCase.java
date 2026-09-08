package com.api.streaming.application.useCases.Order;

import com.api.streaming.domain.interfaces.OrderInterface;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class DeleteExpiredOrdersUseCase {
    private final OrderInterface orderInterface;

    public void execute() {
        this.orderInterface.deleteAllOrdersExpired();
    }
}
