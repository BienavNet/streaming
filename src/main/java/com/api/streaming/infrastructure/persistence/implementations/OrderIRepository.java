package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.infrastructure.mappers.OrderMapper;
import com.api.streaming.infrastructure.persistence.entities.OrderEntity;
import com.api.streaming.infrastructure.persistence.repositories.OrderJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderIRepository implements OrderInterface, OrderDetailsInterface {

    private final OrderJpaRepository jpa;
    private final OrderMapper mapper;

    @Override
    public Optional<Order> getOrderById(OrderId orderId) {
        return this.jpa.findById(orderId.value())
                .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public List<Order> getOrders() {
        List<OrderEntity> entities = this.jpa.findAll();
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public List<Order> getOrdersByAccountId(AccountId accountId) {
        List<OrderEntity> entities = this.jpa.findByAccount(accountId.value());
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public List<Order> getOrdersByAccountIdAndOrderType(AccountId accountId, OrderType orderType) {
        List<OrderEntity> entities = this.jpa.findByAccountAndOrderType(accountId.value(), orderType.getValue());
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public Order saveOrder(Order newOrder) {
        OrderEntity orderToSave = this.mapper.fromDomainToEntity(newOrder);
        OrderEntity orderSaved = this.jpa.save(orderToSave);

        return this.mapper.fromEntityToDomain(orderSaved);
    }

    @Override
    public void deleteOrder(OrderId id) {
        this.jpa.deleteById(id.value());
    }

    @Override
    public List<OrderDetailsQuery> getOrdersDetailsByAccountId(AccountId accountId) {

        return this.jpa.findOrderDetailsByAccountId(accountId.value())
                .stream()
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDetailsQuery> getOrderDetailsById(OrderId orderId) {

        return this.jpa.findOrderDetailsById(orderId.value())
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                });
    }

    @Override
    public List<OrderDetailsQuery> getOrdersDetails() {

        return this.jpa.findAllOrderDetails()
                .stream()
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                })
                .toList();
    }
    
}
