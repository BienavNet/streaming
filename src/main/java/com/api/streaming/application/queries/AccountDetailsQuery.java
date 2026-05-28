package com.api.streaming.application.queries;

import java.time.LocalDate;

import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
 
public record AccountDetailsQuery(
    AccountId id,
    Email email,
    String password,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate,
    LocalDate registeredAt,
    Platform platform,
    Payment payment,
    AccountStatus status
) {}
