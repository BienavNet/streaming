package com.api.streaming.application.useCases.Membership;


import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllMembershipsUseCase {
    private final MembershipDetailsInterface membershipDetailsInterface;

    public Page<MembershipDetailsQuery> execute(Pageable pageable) {

        if (pageable == null)
            pageable = new Pageable(0, 10);

        return this.membershipDetailsInterface.getAllMembershipDetails(pageable);
    }
}
