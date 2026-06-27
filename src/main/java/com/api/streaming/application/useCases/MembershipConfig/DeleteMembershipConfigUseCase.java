package com.api.streaming.application.useCases.MembershipConfig;

import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.valueObjects.MembershipConfigId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteMembershipConfigUseCase {
    private final MembershipConfigInterface membershipConfigInterface;

    public void execute(MembershipConfigId id) {
        this.checkIfExists(id);
        this.membershipConfigInterface.deleteMembershipConfig(id);
    }

    private void checkIfExists(MembershipConfigId id) {
        this.membershipConfigInterface.getMembershipConfig(id)
            .orElseThrow(() -> new EntityNotFoundException("Membership Config does not exist."));
    }
}
