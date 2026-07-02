package com.api.streaming.application.commands;

import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

public record CreateAccountCommand(
    Email email,
    String password,
    PlatformId platform,
    PaymentId payment
) {}
