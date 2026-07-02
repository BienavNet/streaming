package com.api.streaming.application.commands;

import java.time.LocalDate;

import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;

public record CreateMembershipCommand(
    AccountId account,
    MembershipConfigId membershipConfig,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate
) {}
