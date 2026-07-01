package com.api.streaming.application.commands;

import java.time.LocalDate;

import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.valueObjects.AccountId;

public record CreateOrderCommand(
    AccountId account,
    int profileAmount,
    LocalDate buyDate,
    LocalDate finishDate,
    double paymentAmount,
    OrderType orderType
) {}
