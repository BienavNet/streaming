package com.api.streaming.application.commands;

import java.time.LocalDate;

import com.api.streaming.domain.valueObjects.OrderId;

public record UpdateOrderCommand(
    OrderId id,
    Integer profileAmount,
    LocalDate buyDate,
    LocalDate finishDate,
    Double paymentAmount
) {}
