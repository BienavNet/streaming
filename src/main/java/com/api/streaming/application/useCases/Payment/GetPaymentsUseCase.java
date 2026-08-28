package com.api.streaming.application.useCases.Payment;

import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetPaymentsUseCase {
    private final PaymentInterface paymentInterface;

    public Page<Payment> execute(Pageable pageable) {

        if (pageable == null) 
            pageable = new Pageable(0, 10);

        Page<Payment> payments = this.paymentInterface.getPayments(pageable);
        return payments;
    }
}
