package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;

public interface MembershipInterface {
    Optional<Membership> getMembership(MembershipId id);
    List<Membership> getMemberships();
    List<Membership> getMembershipsByAccount(AccountId id);
    Membership saveMembership(Membership membership);
    void deleteMembership(MembershipId id);
}
