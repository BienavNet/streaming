package com.api.streaming.application.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;

public interface MembershipDetailsInterface {
    List<MembershipDetailsQuery> getAllMembershipDetails();
    Optional<MembershipDetailsQuery> getMembershipDetailsByAccount(AccountId accountId);
    Optional<MembershipDetailsQuery> getMembershipDetailsById(MembershipId id);
}
