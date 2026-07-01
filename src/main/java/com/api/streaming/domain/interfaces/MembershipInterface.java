package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.PlatformId;

public interface MembershipInterface {
    Optional<Membership> getMembership(MembershipId id);
    Optional<Membership> getMembershipByAccount(AccountId id);
    List<Membership> getMemberships();
    // List<Membership> getMembershipsByPlatform(PlatformId platformId);
    Membership saveMembership(Membership membership);
    void deleteMembership(MembershipId id);
}
