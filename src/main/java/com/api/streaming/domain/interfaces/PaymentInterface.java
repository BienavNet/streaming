package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;
import com.api.streaming.domain.valueObjects.PaymentId;

public interface PaymentInterface {
    Optional<Payment> getPayment(PaymentId paymentId);
    Optional<Payment> getPaymentByNumber(String number);
    Page<Payment> getPayments(Pageable pageable);
    Payment savePayment(Payment payment);
    void deletePayment(PaymentId paymentId);

    // filters
    List<Payment> findByBankName(String name);
    List<Payment> findByNumber(String number);
}
