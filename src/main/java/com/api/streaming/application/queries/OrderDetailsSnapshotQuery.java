package com.api.streaming.application.queries;

import java.time.LocalDate;

public record OrderDetailsSnapshotQuery(
    String id,
    String accountEmail,
    String platformName,
    String planName,
    Integer profileAmount,
    LocalDate buyDate,
    LocalDate finishDate,
    Double paymentAmount,
    String orderType
) {}
