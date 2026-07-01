package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.commands.UpdateOrderCommand;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.infrastructure.persistence.entities.OrderEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AccountMapper.class}
)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    Order fromEntityToDomain(OrderEntity entity);
    OrderEntity fromDomainToEntity(Order domain);
    List<Order> fromEntityListToDomainList(List<OrderEntity> entities);

    @Mapping(target = "orderDetails", source = "order")
    OrderDetailsQuery fromEntityToQuery(String accountEmail, String platformName, String platformIcon, String plan, OrderEntity order);


    void updateOrderFromCommand(UpdateOrderCommand source, @MappingTarget Order target);


    default OrderId mapFromStringToOrderId(String id) {
        return id!=null ? new OrderId(id) : null;
    }

    default String mapFromOrderIdToString(OrderId id) {
        return id!=null ? id.value() : null;
    }
}
