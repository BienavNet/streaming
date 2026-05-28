package com.api.streaming.infrastructure.dtos.payment;

import com.api.streaming.domain.valueObjects.PaymentId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private PaymentId id;
    private String card;
    private String bankName;
}
