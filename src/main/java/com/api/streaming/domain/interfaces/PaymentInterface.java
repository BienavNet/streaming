package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;

public interface PaymentInterface {
    Optional<Payment> getPayment(PaymentId paymentId);
    List<Payment> getPayments();
    Payment savePayment(Payment payment);
    void deletePayment(PaymentId paymentId);
}
