package com.api.streaming.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.streaming.application.commands.CreateOrderCommand;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.application.useCases.Order.CreateOrderUseCase;
import com.api.streaming.application.useCases.Order.DeleteOrderByIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrderDetailsByIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrdersByAccountIdUseCase;
import com.api.streaming.application.useCases.Order.GetOrdersUseCase;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;
import com.api.streaming.infrastructure.dtos.order.CreateOrderRequest;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.OrderMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OrderController.class)
@Import({
        OrderMapperImpl.class, 
        AccountMapperImpl.class,
        PaymentMapperImpl.class,
        PlatformMapperImpl.class
    }
)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.context.TestConfiguration
    static class JacksonTestConfig {
        @org.springframework.context.annotation.Bean
        public ObjectMapper testObjectMapper() {
            // Esto provee un ObjectMapper básico listo para usar en tus pruebas
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            return mapper;
        }
    }

    @MockitoBean
    private CreateOrderUseCase createOrderUseCase;

    @MockitoBean
    private DeleteOrderByIdUseCase deleteOrderByIdUseCase;

    @MockitoBean
    private GetOrderDetailsByIdUseCase getOrderDetailsByIdUseCase;

    @MockitoBean
    private GetOrdersByAccountIdUseCase getOrdersByAccountIdUseCase;

    @MockitoBean
    private GetOrdersUseCase getOrdersUseCase;

    @Test
    void shouldCreateOrderAndReturnCreated() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest("acc-4565", 1, LocalDate.now(), LocalDate.now().plusMonths(1), 50.0, OrderType.PROFILE.getValue());

        OrderId orderId = new OrderId("order-123");

        when(this.createOrderUseCase.execute(any(CreateOrderCommand.class))).thenReturn(orderId);

        mockMvc.perform(
            post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteOrderAndReturnNoContent() throws Exception {
        String idToDelete = "order-123";
        OrderId orderIdToDelete = new OrderId(idToDelete);

        mockMvc.perform(
            delete("/api/order/{id}", idToDelete)
        )
        .andExpect(status().isNoContent());

        verify(this.deleteOrderByIdUseCase, times(1)).execute(orderIdToDelete);
    }

    @Test
    void shouldGetOrderDetailsAndReturnOk() throws Exception {
        String idToFind = "order-123";
        OrderDetailsQuery mockQuery = new OrderDetailsQuery(new Email("email@gmail.com"), "netflix", "netflix-icon", "basic", new Order(new OrderId(idToFind), new AccountId("acc-45641"), 1, LocalDate.now(), LocalDate.now().plusMonths(1), 50.50, OrderType.PROFILE));

        when(this.getOrderDetailsByIdUseCase.execute(any(OrderId.class))).thenReturn(mockQuery);

        mockMvc.perform(
            get("/api/order/{id}", idToFind)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldGetOrdersFromAnAccountAndReturnOk() throws Exception {
        String accountIdStr = "acc-123";

        when(this.getOrdersByAccountIdUseCase.execute(any(AccountId.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(
            get("/api/order/orders/{account}", accountIdStr)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldGetOrdersDetailsAndReturnOk() throws Exception {
        Pageable pageable = new Pageable(0, 10);
        when(this.getOrdersUseCase.execute(pageable)).thenReturn(new Page<>(Collections.emptyList(), 2L, 1));

        mockMvc.perform(
            get("/api/order/orders")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest("acc-4565", 1, LocalDate.now(), LocalDate.now().plusMonths(1), 50.0, OrderType.PROFILE.getValue());

        when(this.createOrderUseCase.execute(any(CreateOrderCommand.class)))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(
            post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenDeletingOrderThatDoesNotExist() throws Exception {
        String idToDelete = "non-existent";
        OrderId orderIdToDelete = new OrderId(idToDelete);

        doThrow(new EntityNotFoundException("Order not found"))
            .when(this.deleteOrderByIdUseCase)
            .execute(orderIdToDelete);

        mockMvc.perform(
            delete("/api/order/{id}", idToDelete)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenOrderDetailsAreNotFound() throws Exception {
        String idToFind = "non-existent";

        when(this.getOrderDetailsByIdUseCase.execute(any(OrderId.class)))
            .thenThrow(new EntityNotFoundException());

        mockMvc.perform(
            get("/api/order/{id}", idToFind)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    }
}

