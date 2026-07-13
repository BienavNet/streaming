package com.api.streaming.infrastructure.dtos.order;

import java.time.LocalDate;


public record CreateOrderRequest(
    String account,
    int profileAmount,
    LocalDate buyDate,
    LocalDate finishDate,
    double paymentAmount,
    String orderType
) {}
