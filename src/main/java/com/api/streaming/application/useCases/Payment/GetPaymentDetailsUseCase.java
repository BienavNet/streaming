package com.api.streaming.application.useCases.Payment;

import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetPaymentDetailsUseCase {
    private final PaymentInterface paymentInterface;

    public Payment execute(PaymentId id) {
        return this.paymentInterface.getPayment(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found."));
    }
}
