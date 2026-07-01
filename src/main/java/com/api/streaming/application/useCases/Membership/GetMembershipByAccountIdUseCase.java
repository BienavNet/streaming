package com.api.streaming.application.useCases.Membership;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetMembershipByAccountIdUseCase {
    private final MembershipDetailsInterface membershipDetailsInterface;

    public MembershipDetailsQuery execute(AccountId accountId) {
        return this.membershipDetailsInterface.getMembershipDetailsByAccount(accountId)
            .orElseThrow(() -> new EntityNotFoundException("No account is registered to query this membership."));
    }
}
