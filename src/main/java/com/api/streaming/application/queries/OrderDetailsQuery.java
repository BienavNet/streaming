package com.api.streaming.application.queries;

import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.Email;

public record OrderDetailsQuery(
    Email accountEmail,
    String platformName,
    String platformIcon,
    String plan,
    Order orderDetails
) {}
