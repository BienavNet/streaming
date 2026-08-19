package com.api.streaming.infrastructure.strategies.payment;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.streaming.application.interfaces.PaymentFilterStrategy;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;

import lombok.AllArgsConstructor;

@Component("payment_number")
@AllArgsConstructor
public class PaymentNumberFilterStrategy implements PaymentFilterStrategy {
    private final PaymentInterface paymentInterface;

    @Override
    public List<Payment> execute(String criteria) {
        return this.paymentInterface.findByNumber(criteria);
    }
}
