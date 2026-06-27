package com.api.streaming.application.useCases.MembershipConfig;

import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetMembershipConfigUseCase {
    private final MembershipConfigInterface membershipConfigInterface;

    public MembershipConfig execute(MembershipConfigId id) {
        return this.membershipConfigInterface.getMembershipConfig(id)
            .orElseThrow(() -> new EntityNotFoundException("Membership Config not found."));
    }
}
