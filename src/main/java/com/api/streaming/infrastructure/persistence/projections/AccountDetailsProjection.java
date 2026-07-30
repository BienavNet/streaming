package com.api.streaming.infrastructure.persistence.projections;

import java.time.LocalDate;

public interface AccountDetailsProjection
{
    String getId();
    String getEmail();
    String getPassword();
    Boolean getIsFreeTrial();
    LocalDate getMembershipBuyDate();
    LocalDate getMembershipExpireDate();
    LocalDate getRegisteredAt();
    String getPlatformId();
    String getPlatformName();
    String getPlatformLink();
    String getPlatformIcon();
    String getPaymentId();
    String getPaymentNumber();
    Integer getPaymentMonth();
    Integer getPaymentYear();
    String getPaymentCvv();
    String getPaymentBankName();
    String getMembershipId();
    String getMembershipConfigId();
    String getConfigPlan();
    Integer getConfigProfiles();
}
