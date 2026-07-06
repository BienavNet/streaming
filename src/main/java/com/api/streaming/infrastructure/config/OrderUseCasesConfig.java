package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.useCases.Order.CreateOrderUseCase;
import com.api.streaming.application.useCases.Order.DeleteOrderByIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrderDetailsByIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrdersByAccountIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrdersUseCase;
import com.api.streaming.application.useCases.Order.UpdateOrderUseCase;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.infrastructure.mappers.OrderMapper;

@Configuration
public class OrderUseCasesConfig {
    
    @Bean
    CreateOrderUseCase createOrderUseCase(
        OrderInterface orderInterface,
        GenerateIdInterface generateIdInterface,
        MembershipConfigInterface membershipConfigInterface,
        MembershipInterface membershipInterface
    ) {
        return new CreateOrderUseCase(orderInterface, generateIdInterface, membershipConfigInterface, membershipInterface);
    }

    @Bean
    DeleteOrderByIdUseCase deleteOrderByIdUseCase(
        OrderInterface orderInterface
    ) {
        return new DeleteOrderByIdUseCase(orderInterface);
    }

    @Bean
    GetOrderDetailsByIdUseCase getOrderDetailsByIdUseCase(
        OrderDetailsInterface orderDetailsInterface
    ) {
        return new GetOrderDetailsByIdUseCase(orderDetailsInterface);
    }

    @Bean 
    GetOrdersByAccountIdUseCase getOrdersByAccountIdUseCase(
        OrderDetailsInterface orderDetailsInterface
    ){
        return new GetOrdersByAccountIdUseCase(orderDetailsInterface);
    }

    @Bean
    GetOrdersUseCase getOrdersUseCase(
        OrderDetailsInterface orderDetailsInterface
    ) {
        return new GetOrdersUseCase(orderDetailsInterface);
    }

    @Bean
    UpdateOrderUseCase updateOrderUseCase(
        OrderInterface orderInterface,
        OrderMapper mapper,
        MembershipInterface membershipInterface,
        MembershipConfigInterface membershipConfigInterface

    ) {
        return new UpdateOrderUseCase(orderInterface, mapper, membershipInterface, membershipConfigInterface);
    }
}
