package com.api.streaming.application.useCases.Payment;

import com.api.streaming.application.commands.CreatePaymentCommand;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePaymentUseCase {
    private final PaymentInterface paymentInterface;
    private final GenerateIdInterface generateIdInterface;

    public PaymentId execute(CreatePaymentCommand command) {

        this.checkIfNumberIsAlreadyRegistered(command.number());

        PaymentId id = this.generateIdInterface.generatePaymentId();

        Payment createdPayment = new Payment(id, command.number(), command.month(), command.year(), command.cvv(), command.bankName());

        this.paymentInterface.savePayment(createdPayment);

        return id;
    }

    private void checkIfNumberIsAlreadyRegistered(String number) {
        this.paymentInterface.getPaymentByNumber(number)
            .ifPresent( entity ->  {
                throw new EntityExistsException("A payment has been registered with this number and you cannot register it again.");
            });
    }
}
 