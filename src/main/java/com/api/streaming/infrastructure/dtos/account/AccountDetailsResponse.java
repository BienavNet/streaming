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
public class AccountDetailsResponse {
    private String id;
    private String email;
    // private String password;
    private LocalDate membershipExpireDate;
    private String platformName;
    private String status;
}
