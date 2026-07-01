package com.api.streaming.application.useCases.Membership;

import java.util.Optional;

import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteMembershipByAccountUseCase {
    private final MembershipInterface membershipInterface;

    // external service

    public void execute(AccountId accountId) {
        Membership membershipToDelete = this.checkIfAccountHasAMembershipRegistered(accountId);

        this.membershipInterface.deleteMembership(membershipToDelete.getId());
    }

    private Membership checkIfAccountHasAMembershipRegistered(AccountId accountId) {
        Optional<Membership> optionalMembership = this.membershipInterface.getMembershipByAccount(accountId);

        if (optionalMembership.isEmpty())
            throw new EntityNotFoundException("Cannot delete a membership that is not registered.");

        return optionalMembership.get();

    }
}
