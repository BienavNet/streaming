package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;

public interface OrderInterface {
    Optional<Order> getOrderById(OrderId orderId);
    List<Order> getOrders();
    List<Order> getOrdersByAccountId(AccountId accountId);
    List<Order> getOrdersByAccountIdAndOrderType(AccountId accountId, OrderType orderType);
    Order saveOrder(Order newOrder);
    void deleteOrder(OrderId id);
}
