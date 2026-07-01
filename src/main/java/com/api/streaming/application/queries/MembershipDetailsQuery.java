package com.api.streaming.application.queries;

import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipId;

public record MembershipDetailsQuery(
    MembershipId id,
    MembershipConfig details
) {}
