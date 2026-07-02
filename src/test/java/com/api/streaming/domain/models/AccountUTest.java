package com.api.streaming.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

public class AccountUTest {

    private Account account;

    @BeforeEach
    void setUp() {
        AccountId accountId = new AccountId("123asdfa");
        PlatformId platformId = new PlatformId("50asdf");
        PaymentId paymentId = new PaymentId("234xasdf");
        Email email = new Email("test@example.com");
        AccountStatus status = AccountStatus.ACTIVE;
        LocalDate membershipStart = LocalDate.now();
        LocalDate membershipEnd = LocalDate.of(2050, 6, 29);
        
        this.account = new Account(accountId, email, "123456789", LocalDate.now(), platformId, paymentId, status);
    }

    @Test
    void shouldCreateValidAccount() {
        assertThat(this.account).isNotNull().isInstanceOf(Account.class);
    }

    @Test
    void shouldDeactivateAndActivateAccount(){
        this.account.deactivateAccount();
        assertThat(this.account.getStatus().getValue().toLowerCase()).isEqualTo(AccountStatus.DEACTIVE.getValue().toLowerCase());

        this.account.activateAccount();
        assertThat(this.account.getStatus().getValue().toLowerCase()).isEqualTo(AccountStatus.ACTIVE.getValue().toLowerCase());
    }

    @Test
    void shouldThrowExceptionDeactivatingWhenAccountIsAlreadyDeactivated() {
        this.account.deactivateAccount();
        assertThatThrownBy(() -> this.account.deactivateAccount())
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionActivatingWhenAccountIsAlreadyActivated() {
        assertThatThrownBy(() -> this.account.activateAccount())
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAccountWithIncorrectValues() {
        assertThatThrownBy(() -> this.account.setEmail(null))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.account.setPlatform(null))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> this.account.setPayment(null))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAccountWithCorrectValues() {
        this.account.setEmail("email@email.com");
        assertThat(this.account.getEmail().value())
        .isEqualTo("email@email.com");

        this.account.setPlatform("23lk4j2");
        assertThat(this.account.getPlatform().value())
        .isEqualTo("23lk4j2");

        this.account.setPayment("23lk4jl");
        assertThat(this.account.getPayment().value())
        .isEqualTo("23lk4jl");
    }


}
