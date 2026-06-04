package com.api.streaming.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.streaming.application.commands.CreatePaymentCommand;
import com.api.streaming.application.useCases.Payment.CreatePaymentUseCase;
import com.api.streaming.application.useCases.Payment.DeletePaymentUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentDetailsUseCase;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.infrastructure.dtos.payment.CreatePaymentRequest;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;

import jakarta.persistence.EntityNotFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PaymentController.class)
@Import({PaymentMapperImpl.class})
public class PaymentControllerITest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreatePaymentUseCase createPaymentUseCase;

    @MockitoBean
    private DeletePaymentUseCase deletePaymentUseCase;

    @MockitoBean
    private GetPaymentDetailsUseCase getPaymentDetailsUseCase;

    @Test
    void shouldCreatePaymentAndReturnOK() throws Exception {
        CreatePaymentRequest request = new CreatePaymentRequest("4554454545545445", 10, 2026, "123", "");
        
        PaymentId paymentId = new PaymentId("uuid-239842");
        
        when(this.createPaymentUseCase.execute(any(CreatePaymentCommand.class))).thenReturn(paymentId);


        mockMvc.perform(
            post("/api/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk());

    }

    @Test
    void shouldDeletePaymentAndReturnOk() throws Exception {
        String idToDelete = "uuid-92342";

        PaymentId paymentIdToDelete = new PaymentId(idToDelete);

        mockMvc.perform(
            delete("/api/payment/{id}", idToDelete)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(idToDelete));

        verify(this.deletePaymentUseCase, times(1)).execute(paymentIdToDelete);
    }

    @Test
    void shouldGetPaymentDetailsAndReturnOk() throws Exception {
        String idToFind = "uuid-89234";
        Payment mockPayment = new Payment(new PaymentId(idToFind), "4545455454454554", 10, 2026, "000", "");

        when(this.getPaymentDetailsUseCase.execute(any(PaymentId.class))).thenReturn(mockPayment);

        mockMvc.perform(
            get("/api/payment/{id}", idToFind)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingPayment() throws JacksonException, Exception {
        CreatePaymentRequest request = new CreatePaymentRequest("4554454545545445", 10, 2026, "123", "");

        when(this.createPaymentUseCase.execute(any(CreatePaymentCommand.class))).thenThrow(
            new IllegalArgumentException()
        );

        mockMvc.perform(
            post("/api/payment")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenDeletingPaymentThatDoesNotExist() throws Exception {
        String idToDelete = "uuid-92342";

        PaymentId paymentIdToDelete = new PaymentId(idToDelete);

        doThrow(new EntityNotFoundException("Payment not found"))
        .when(this.deletePaymentUseCase)
        .execute(paymentIdToDelete);

        mockMvc.perform(
            delete("/api/payment/{id}", idToDelete)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenPaymentIsNotFound() throws Exception {
        String idToFind = "uuid-89234";

        when(this.getPaymentDetailsUseCase.execute(any(PaymentId.class))).thenThrow(new EntityNotFoundException());

        mockMvc.perform(
            get("/api/payment/{id}", idToFind)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
