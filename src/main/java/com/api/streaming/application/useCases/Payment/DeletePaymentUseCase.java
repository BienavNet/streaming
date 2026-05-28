package com.api.streaming.application.useCases.Payment;

import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.valueObjects.PaymentId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeletePaymentUseCase {
    private final PaymentInterface paymentInterface;


    public void execute(PaymentId id) {
        this.checkIfExists(id);

        this.paymentInterface.deletePayment(id);
    }

    private void checkIfExists(PaymentId id) {
        this.paymentInterface.getPayment(id)
            .orElseThrow(()-> new EntityNotFoundException("Payment couldn't be deleted because it isn't registered."));
    }
}
