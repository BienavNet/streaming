package com.api.streaming.application.useCases.Membership;

import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.mappers.MembershipMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateMembershipUseCase {
    private final MembershipInterface membershipInterface;
    private final MembershipMapper mapper;

    public MembershipId execute(UpdateMembershipCommand command) {
        Membership membershipToUpdate = this.checkIfMembershipExists(command.membershipId());

        this.mapper.updateMembershipFromCommand(command, membershipToUpdate);
        
        this.membershipInterface.saveMembership(membershipToUpdate);

        return membershipToUpdate.getId();
    }

    private Membership checkIfMembershipExists(MembershipId membershipId) {
        return this.membershipInterface.getMembership(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot update membership because that does not exist."));
    }
}
