package com.api.streaming.domain.models;

import java.time.LocalDate;


import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private AccountId id;
    private Email email;
    private String password;
    private boolean isFreeTrial;
    private LocalDate membershipBuyDate;
    private LocalDate membershipExpireDate;
    private LocalDate registeredAt;
    private PlatformId platform;
    private PaymentId payment;
    private AccountStatus status;
    // private User createdBy;
    
    public Account(AccountId id, Email email, String password, boolean isFreeTrial, LocalDate membershipBuyDate,
        LocalDate membershipExpireDate, LocalDate registeredAt, PlatformId platform, PaymentId payment,
        AccountStatus status) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.isFreeTrial = isFreeTrial;
            this.membershipBuyDate = membershipBuyDate;
            this.membershipExpireDate = membershipExpireDate;
            this.registeredAt = registeredAt;
            this.platform = platform;
            this.payment = payment;
            this.status = status;

            this.validate();
    }

    public void deactivateAccount() {
        if (this.status.equals(AccountStatus.fromValue(AccountStatus.DEACTIVE.getValue())))
            throw new IllegalArgumentException("Account is already deactivated.");
        this.status = AccountStatus.fromValue(AccountStatus.DEACTIVE.getValue());
    }


    private void validate() {
        this.validateMembershipDates();
    }

    private void validateMembershipDates() {
        if (this.membershipExpireDate.isBefore(this.membershipBuyDate))
            throw new IllegalArgumentException("Expired date cannot be before that buy membership date.");
    }


}
    