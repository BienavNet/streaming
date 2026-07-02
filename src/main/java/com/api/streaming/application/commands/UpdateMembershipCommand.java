package com.api.streaming.application.commands;

import java.time.LocalDate;

import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;

public record UpdateMembershipCommand(
    MembershipId membershipId,
    MembershipConfigId membershipConfigId,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate
) {}
