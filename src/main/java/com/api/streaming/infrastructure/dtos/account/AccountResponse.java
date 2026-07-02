package com.api.streaming.infrastructure.dtos.account;

import java.time.LocalDate;

import com.api.streaming.infrastructure.dtos.payment.PaymentResponse;
import com.api.streaming.infrastructure.dtos.platform.PlatformResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private String email;
    private String password;
    // private boolean isFreeTrial;
    // private LocalDate membershipBuyDate;
    // private LocalDate membershipExpireDate;
    private LocalDate registeredAt;
    private PlatformResponse platform;
    private PaymentResponse payment;
    private String status;
}
