package com.api.streaming.infrastructure.persistence.projections;

import java.time.LocalDate;

public interface MembershipDetailsProjection {
    String getId();
    String getMembershipConfigId();
    String getPlatformId();
    String getPlan();
    Integer getProfiles();
    boolean getIsFreeTrial();
    LocalDate getMembershipBuyDate();
    LocalDate getMembershipExpireDate();
}
