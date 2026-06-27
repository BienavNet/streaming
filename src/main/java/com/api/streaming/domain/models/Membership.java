package com.api.streaming.domain.models;

import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Membership {
    private MembershipId id;
    private AccountId account;
    private MembershipConfigId membershipConfig;

    public Membership(MembershipId id, AccountId account, MembershipConfigId membershipConfig) {
        this.id = id;
        this.account = account;
        this.membershipConfig = membershipConfig;
    }

}
