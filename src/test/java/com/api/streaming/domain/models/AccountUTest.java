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
        
        this.account = new Account(accountId, email, "123456789", LocalDate.now(), platformId, paymentId);
    }

    @Test
    void shouldCreateValidAccount() {
        assertThat(this.account).isNotNull().isInstanceOf(Account.class);
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
    void shouldUpdateAccountWithCorrectValues() {
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
