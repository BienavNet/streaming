package com.api.streaming.application.interfaces;

import java.util.List;

import com.api.streaming.domain.models.Payment;

public interface PaymentFilterStrategy {
    List<Payment> execute(String criteria);
}
