package com.api.streaming.application.useCases.Payment;

import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.valueObjects.PaymentId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeletePaymentUseCase {
    private final PaymentInterface paymentInterface;

    //Nueva interfaz para validaciones
    private final AccountInterface accountInterface;


    public void execute(PaymentId id) {
        this.checkIfExists(id);
        this.checkIfTheresNoAccountBindedToThisPayment(id);

        this.paymentInterface.deletePayment(id);
    }

    private void checkIfExists(PaymentId id) {
        this.paymentInterface.getPayment(id)
            .orElseThrow(()-> new EntityNotFoundException("Payment couldn't be deleted because it isn't registered."));
    }

    private void checkIfTheresNoAccountBindedToThisPayment(PaymentId id) {
        int listLength = this.accountInterface.getAccountsByPaymentId(id).size();

        if (listLength != 0)
            throw new IllegalArgumentException("Payment couldn't be deleted because there's account registered with this payment method.");
    }
}
