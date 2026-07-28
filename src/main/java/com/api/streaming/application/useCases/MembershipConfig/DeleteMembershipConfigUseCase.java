package com.api.streaming.application.useCases.MembershipConfig;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.valueObjects.MembershipConfigId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteMembershipConfigUseCase {
    private final MembershipConfigInterface membershipConfigInterface;

    //Repo para validar el cascade deletion
    private final MembershipDetailsInterface membershipDetailsInterface;

    public void execute(MembershipConfigId id) {
        this.checkIfExists(id);
        this.cascadeEntitiesValidation(id);
        this.membershipConfigInterface.deleteMembershipConfig(id);
    }

    private void checkIfExists(MembershipConfigId id) {
        this.membershipConfigInterface.getMembershipConfig(id)
            .orElseThrow(() -> new EntityNotFoundException("Membership Config does not exist."));
    }

    private void cascadeEntitiesValidation(MembershipConfigId id) {
        if (!this.membershipDetailsInterface.getMembershipsByMembershipConfig(id).isEmpty())
            throw new EntityExistsException("Cannot delete this membership config because there're memberships registered with this.");
    }
}
