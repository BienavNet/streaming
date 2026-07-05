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
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.entities.OrderEntity;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.repositories.AccountJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.OrderJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.PlatformJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderIRepository implements OrderInterface, OrderDetailsInterface {

    private final OrderJpaRepository jpa;
    private final OrderMapper mapper;

    //External repositories
    private final AccountJpaRepository accountJpaRepository;
    private final PlatformJpaRepository platformJpaRepository;
    private final MembershipConfigJpaRepository membershipConfigJpaRepository;
    private final MembershipJpaRepository membershipJpaRepository;

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
        List<OrderEntity> entities = this.jpa.findByIdAndOrderType(accountId.value(), orderType.getValue());
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
        AccountEntity accountEntity = this.accountJpaRepository.findById(accountId.value()).get();
        PlatformEntity platformEntity = this.platformJpaRepository.findById(accountEntity.getPlatform()).get();

        MembershipEntity membershipEntity = this.membershipJpaRepository.findByAccount(accountId.value()).get();

        MembershipConfigEntity config = this.membershipConfigJpaRepository.findById(membershipEntity.getMembershipConfig()).get();

        List<OrderEntity> orders = this.jpa.findByAccount(accountId.value());

        return orders
        .stream()
        .map(order -> {
            return this.mapper.fromEntityToQuery(accountEntity.getEmail(), platformEntity.getName(), platformEntity.getIcon(), config.getPlan(), order);
        })
        .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDetailsQuery> getOrderDetailsById(OrderId orderId) {
        return this.jpa.findById(orderId.value())
                .map(order -> {
                    AccountEntity  accountEntity = this.accountJpaRepository.findById(order.getAccount()).get();
                    PlatformEntity platformEntity = this.platformJpaRepository.findById(accountEntity.getPlatform()).get();
                    MembershipEntity membershipEntity = this.membershipJpaRepository.findByAccount(order.getAccount()).get();
                    MembershipConfigEntity configEntity = this.membershipConfigJpaRepository.findById(membershipEntity.getMembershipConfig()).get();
                    return this.mapper.fromEntityToQuery(accountEntity.getEmail(), platformEntity.getName(), platformEntity.getIcon(), configEntity.getPlan(), order);
                });
    }

    @Override
    public List<OrderDetailsQuery> getOrdersDetails() {
        return this.jpa.findAll()
                .stream()
                .map(order -> {
                    AccountEntity  accountEntity = this.accountJpaRepository.findById(order.getAccount()).get();
                    PlatformEntity platformEntity = this.platformJpaRepository.findById(accountEntity.getPlatform()).get();
                    MembershipEntity membershipEntity = this.membershipJpaRepository.findByAccount(order.getAccount()).get();
                    MembershipConfigEntity configEntity = this.membershipConfigJpaRepository.findById(membershipEntity.getMembershipConfig()).get();
                    return this.mapper.fromEntityToQuery(accountEntity.getEmail(), platformEntity.getName(), platformEntity.getIcon(), configEntity.getPlan(), order);
                })
                .collect(Collectors.toList());
    }
    
}
