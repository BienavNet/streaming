package com.api.streaming.application.interfaces.strategies;

import java.util.List;

import com.api.streaming.domain.models.Payment;

public interface PaymentFilterStrategy {
    List<Payment> execute(String criteria);
}
