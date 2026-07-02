package com.api.streaming.domain.models;

import java.time.LocalDate;

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
    private boolean isFreeTrial;
    private LocalDate membershipBuyDate;
    private LocalDate membershipExpireDate;

    public Membership(MembershipId id, AccountId account, MembershipConfigId membershipConfig, boolean isFreeTrial, LocalDate membershipBuyDate,
        LocalDate membershipExpireDate) {
        this.id = id;
        this.account = account;
        this.membershipConfig = membershipConfig;
        this.isFreeTrial = isFreeTrial;
        this.membershipBuyDate = membershipBuyDate;
        this.membershipExpireDate = membershipExpireDate;

        this.validate();
    }

    public void setMembershipBuyDate(LocalDate date) {
        if (date != null && date.isBefore(this.membershipExpireDate)){
            this.membershipBuyDate = date;
            return;
        }
        throw new IllegalArgumentException("The new buy date has to be before than expired date.");
    }

    public void setMembershipExpireDate(LocalDate date) {
        if ( date!=null && date.isAfter(this.membershipBuyDate)) {
            this.membershipExpireDate = date;
            return;
        }
        throw new IllegalArgumentException("The new expire date has to be after than expired date.");
    }

    private void validateMembershipDates() {
        if (this.membershipExpireDate.isBefore(this.membershipBuyDate))
            throw new IllegalArgumentException("Expired date cannot be before that buy membership date.");
    }

    private void validate() {
        this.validateMembershipDates();
    }

}
