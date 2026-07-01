package com.api.streaming.application.useCases.Membership;

import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateMembershipUseCase {
    private final MembershipInterface membershipInterface;

    // External Service
    private final MembershipConfigInterface membershipConfigInterface;

    public MembershipId execute(UpdateMembershipCommand command) {
        this.checkIfMembershipConfigExists(command.membershipConfigId());
        Membership membershipToUpdate = this.checkIfMembershipExists(command.membershipId());
        membershipToUpdate.setMembershipConfig(command.membershipConfigId());
        
        this.membershipInterface.saveMembership(membershipToUpdate);

        return membershipToUpdate.getId();
    }

    private Membership checkIfMembershipExists(MembershipId membershipId) {
        return this.membershipInterface.getMembership(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot update membership because that does not exist."));
    }

    private void checkIfMembershipConfigExists(MembershipConfigId membershipConfig) {
        this.membershipConfigInterface.getMembershipConfig(membershipConfig)
            .orElseThrow(() -> new EntityNotFoundException("You cannot assign this config to this membership, because this config does not exist."));
    }
}
