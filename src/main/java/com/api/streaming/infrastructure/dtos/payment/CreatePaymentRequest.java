package com.api.streaming.infrastructure.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    private String number;
    private Integer month;
    private Integer year;
    private String cvv;
    private String bankName;
}
