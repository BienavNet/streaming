package com.api.streaming.infrastructure.dtos.order;

import java.time.LocalDate;

public record OrderResponse(
    String id,
    String accountEmail,
    String platformName,
    String platformIcon,
    LocalDate buyDate,
    String orderType
) {}
