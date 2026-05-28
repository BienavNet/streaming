package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreatePaymentCommand;
import com.api.streaming.application.useCases.Payment.CreatePaymentUseCase;
import com.api.streaming.application.useCases.Payment.DeletePaymentUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentDetailsUseCase;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.payment.CreatePaymentRequest;
import com.api.streaming.infrastructure.dtos.payment.PaymentResponse;
import com.api.streaming.infrastructure.mappers.PaymentMapper;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final CreatePaymentUseCase createPaymentUseCase;
    private final DeletePaymentUseCase deletePaymentUseCase;
    private final GetPaymentDetailsUseCase getPaymentDetailsUseCase;
    private final PaymentMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        CreatePaymentCommand command = this.mapper.createPaymentFromDtoToCommand(request);

        String id = this.createPaymentUseCase.execute(command).value();

        GenericResponse response = GenericResponse.builder().id(id).status(HttpStatus.OK.value()).build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deletePayment(@PathVariable String id) {
        PaymentId paymentId = new PaymentId(id);

        this.deletePaymentUseCase.execute(paymentId);
        GenericResponse response = GenericResponse
                                    .builder()
                                    .id(id)
                                    .status(HttpStatus.OK.value())
                                    .build();
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
        PaymentId paymentId = new PaymentId(id);

        Payment payment = this.getPaymentDetailsUseCase.execute(paymentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.mapper.fromDomainToPaymentResponse(payment));

        
    }
    
    
}
