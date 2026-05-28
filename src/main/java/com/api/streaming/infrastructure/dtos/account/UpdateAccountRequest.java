package com.api.streaming.infrastructure.dtos.account;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    private String email;
    private String password;
    private boolean isFreeTrial;
    private LocalDate membershipBuyDate;
    private LocalDate membershipExpireDate;
    private String platform;
    private String payment;
}
