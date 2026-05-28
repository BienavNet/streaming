package com.api.streaming.application.commands;

import java.time.LocalDate;

import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

public record CreateAccountCommand(
    Email email,
    String password,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate,
    PlatformId platform,
    PaymentId payment
) {}
