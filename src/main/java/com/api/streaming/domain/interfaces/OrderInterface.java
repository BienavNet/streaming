package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.OrderId;

public interface OrderInterface {
    Optional<Order> getOrder(OrderId orderId);
    List<Order> getOrders();
    Order saveOrder(Order newOrder);
    void deleteOrder(OrderId id);
}
