package com.api.streaming.infrastructure.persistence.projections;

public interface OrderDetailsProjection {
    String getAccountEmail();
    String getPlatformName();
    String getPlatformIcon();
    String getPlan();
    String getId();
    String getAccountId();
    String getProfileAmount();
    String getBuyDate();
    String getFinishDate();
    String getPaymentAmount();
    String getOrderType();
}
