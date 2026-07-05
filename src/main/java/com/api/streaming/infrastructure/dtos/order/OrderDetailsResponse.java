package com.api.streaming.infrastructure.dtos.order;

import java.time.LocalDate;

public record OrderDetailsResponse(
    String id,
    String accountEmail,
    String platformName,
    String platformIcon,
    String plan,
    Integer profileAmount,
    LocalDate buyDate,
    LocalDate finishDate,
    Double paymentAmount,
    String orderType
) {}
