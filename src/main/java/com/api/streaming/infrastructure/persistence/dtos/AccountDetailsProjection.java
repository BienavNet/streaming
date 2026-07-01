package com.api.streaming.infrastructure.persistence.dtos;

import java.time.LocalDate;

public record AccountDetailsProjection(
    String id,
    String email,
    String password,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate,
    LocalDate registeredAt,
    String platformId,
    String platformName,
    String platformLink,
    String platformIcon,
    String paymentId,
    String paymentNumber,
    String paymentMonth,
    String paymentYear,
    String paymentCvv,
    String paymentBankName,
    String status,
    String membershipId,
    String membershipConfigId,
    String plan,
    Integer profiles
) {}
