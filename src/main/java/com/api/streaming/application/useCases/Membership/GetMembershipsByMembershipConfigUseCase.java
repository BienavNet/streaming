package com.api.streaming.application.useCases.Membership;

import java.util.List;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.valueObjects.MembershipConfigId;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetMembershipsByMembershipConfigUseCase {
    
    private final MembershipDetailsInterface membershipInterface;

    public List<MembershipDetailsQuery> execute(MembershipConfigId id) {
        return this.membershipInterface.getMembershipsByMembershipConfig(id);
    }
}
