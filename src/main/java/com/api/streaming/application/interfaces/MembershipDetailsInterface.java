package com.api.streaming.application.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

public interface MembershipDetailsInterface {
    Page<MembershipDetailsQuery> getAllMembershipDetails(Pageable pageable);
    List<MembershipDetailsQuery> getMembershipsByMembershipConfig(MembershipConfigId id);
    Optional<MembershipDetailsQuery> getMembershipDetailsByAccount(AccountId accountId);
    Optional<MembershipDetailsQuery> getMembershipDetailsById(MembershipId id);
}
