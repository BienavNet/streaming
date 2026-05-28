package com.api.streaming.application.commands;

public record CreatePaymentCommand(
    String number,
    Integer month,
    Integer year,
    String cvv,
    String bankName
) {}
