package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreatePaymentCommand;
import com.api.streaming.application.useCases.Payment.CreatePaymentUseCase;
import com.api.streaming.application.useCases.Payment.DeletePaymentUseCase;
import com.api.streaming.application.useCases.Payment.FilterPaymentsUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentDetailsUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentsUseCase;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.payment.CreatePaymentRequest;
import com.api.streaming.infrastructure.dtos.payment.PaymentResponse;
import com.api.streaming.infrastructure.mappers.PaymentMapper;

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
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final CreatePaymentUseCase createPaymentUseCase;
    private final DeletePaymentUseCase deletePaymentUseCase;
    private final GetPaymentDetailsUseCase getPaymentDetailsUseCase;
    private final GetPaymentsUseCase getPaymentsUseCase;
    private final FilterPaymentsUseCase filterPaymentsUseCase;
    private final PaymentMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse<PaymentResponse>> createPayment(@RequestBody CreatePaymentRequest request) {
        CreatePaymentCommand command = this.mapper.createPaymentFromDtoToCommand(request);

        PaymentId createdPaymentId = this.createPaymentUseCase.execute(command);

        Payment createdPayment = this.getPaymentDetailsUseCase.execute(createdPaymentId);

        GenericResponse<PaymentResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setId(createdPayment.getId().value());
        response.setMessage("Payment created successfully.");
        response.setData(this.mapper.fromDomainToDto(createdPayment));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        PaymentId paymentId = new PaymentId(id);

        this.deletePaymentUseCase.execute(paymentId);
        
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<PaymentResponse>> getPayment(@PathVariable String id) {
        PaymentId paymentId = new PaymentId(id);

        Payment payment = this.getPaymentDetailsUseCase.execute(paymentId);

        GenericResponse<PaymentResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainToDto(payment));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<GenericResponse<List<PaymentResponse>>> getAllPayments() {
        List<Payment> payments = this.getPaymentsUseCase.execute();

        GenericResponse<List<PaymentResponse>> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainListToDtoList(payments));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping("/filter/{field}/{criteria}")
    public ResponseEntity<GenericResponse<List<PaymentResponse>>> filterPayments(@PathVariable String field, @PathVariable String criteria) {
        List<Payment> paymentsFiltered = this.filterPaymentsUseCase.execute(field, criteria);

        GenericResponse<List<PaymentResponse>> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainListToDtoList(paymentsFiltered));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    
    
    
}
