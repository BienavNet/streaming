package com.api.streaming.infrastructure.persistence.projections;

import java.time.LocalDate;

public interface OrderDetailsProjection {
    String getAccountEmail();
    String getPlatformName();
    String getPlatformIcon();
    String getPlan();
    String getId();
    String getAccount();
    Integer getProfileAmount();
    LocalDate getBuyDate();
    LocalDate getFinishDate();
    Double getPaymentAmount();
    String getOrderType();
}
