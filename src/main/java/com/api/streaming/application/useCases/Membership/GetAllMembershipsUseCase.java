package com.api.streaming.application.useCases.Membership;

import java.util.List;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllMembershipsUseCase {
    private final MembershipDetailsInterface membershipDetailsInterface;

    public List<MembershipDetailsQuery> execute() {
        return this.membershipDetailsInterface.getAllMembershipDetails();
    }
}
