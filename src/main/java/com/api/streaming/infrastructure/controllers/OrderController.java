package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreateOrderCommand;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.application.useCases.Order.CreateOrderUseCase;
import com.api.streaming.application.useCases.Order.DeleteOrderByIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrderDetailsByIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrdersByAccountIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrdersUseCase;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.order.CreateOrderRequest;
import com.api.streaming.infrastructure.dtos.order.OrderDetailsResponse;
import com.api.streaming.infrastructure.dtos.order.OrderResponse;
import com.api.streaming.infrastructure.mappers.OrderMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;





@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final DeleteOrderByIdUseCase deleteOrderByIdUseCase;
    private final GetOrderDetailsByIdUseCase getOrderDetailsByIdUseCase;
    private final GetOrdersByAccountIdUseCase getOrdersByAccountIdUseCase;
    private final GetOrdersUseCase getOrdersUseCase;

    private final OrderMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse<String>> saveOrder(@RequestBody CreateOrderRequest request) {
        
        CreateOrderCommand command = this.mapper.fromCreateOrderRequestToCreateOrderCommand(request);
        
        OrderId orderId = this.createOrderUseCase.execute(command);

        GenericResponse<String> response = new GenericResponse<String>(orderId.value(), HttpStatus.CREATED.value(), "Order created successfully.", orderId.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteOrder(@PathVariable String id) {
        OrderId orderId = new OrderId(id);

        this.deleteOrderByIdUseCase.execute(orderId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<OrderDetailsResponse>> getOrderDetails(@PathVariable String id) {
        
        OrderId orderId = new OrderId(id);
        OrderDetailsQuery order = this.getOrderDetailsByIdUseCase.execute(orderId);

        OrderDetailsResponse details = this.mapper.fromQueryToResponseDto(order);

        GenericResponse<OrderDetailsResponse> response = new GenericResponse<OrderDetailsResponse>(id, HttpStatus.OK.value(), null, details);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/orders/{account}")
    public ResponseEntity<GenericResponse<List<OrderDetailsResponse>>> getOrdersFromAnAccount(@PathVariable String account) {
        
        AccountId accountId = new AccountId(account);

        List<OrderDetailsQuery> orders = this.getOrdersByAccountIdUseCase.execute(accountId);

        List<OrderDetailsResponse> ordersResponse = this.mapper.fromQueryListToResponseDtoList(orders);

        GenericResponse<List<OrderDetailsResponse>> response = new GenericResponse<List<OrderDetailsResponse>>(account, HttpStatus.OK.value(), null, ordersResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/orders/{page}/{offset}")
    public ResponseEntity<GenericResponse<Page<OrderResponse>>> getOrdersDetails(@PathVariable int page, @PathVariable int offset) throws Exception {

        Pageable pageable = new Pageable(page, offset);

        Page<OrderResponse> orders = this.getOrdersUseCase.execute(pageable)
            .map(mapper::orderDetailsQueryToOrderResponse);

        GenericResponse<Page<OrderResponse>> response = new GenericResponse<>();

        response.setStatus(HttpStatus.OK.value());
        response.setData(orders);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    

}
