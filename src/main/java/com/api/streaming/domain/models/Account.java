package com.api.streaming.domain.models;

import java.time.LocalDate;


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
    private LocalDate registeredAt;
    private PlatformId platform;
    private PaymentId payment;
    
    public Account(AccountId id, Email email, String password, LocalDate registeredAt, PlatformId platform, PaymentId payment) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.registeredAt = registeredAt;
            this.platform = platform;
            this.payment = payment;

            this.validate();
    }

    public void setEmail(String rawEmail) {
        this.email = new Email(rawEmail);
    }

    public void setPlatform(String rawPlatform) {
        this.platform = new PlatformId(rawPlatform);
    }

    public void setPayment(String rawPaymentId) {
        this.payment = new PaymentId(rawPaymentId);
    }


    private void validate() {
        // this.validateMembershipDates();
    }

    


}
    