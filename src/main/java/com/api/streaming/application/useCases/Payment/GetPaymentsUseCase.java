package com.api.streaming.application.useCases.Payment;

import java.util.List;

import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetPaymentsUseCase {
    private final PaymentInterface paymentInterface;

    public List<Payment> execute() {
        List<Payment> payments = this.paymentInterface.getPayments();
        return payments;
    }
}
