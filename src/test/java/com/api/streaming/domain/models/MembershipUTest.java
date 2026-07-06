package com.api.streaming.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;

public class MembershipUTest {
    private Membership membership;


    @BeforeEach
    void setup() {
        this.membership = new Membership(new MembershipId("uuid-92384"), new AccountId("82348-23948"), new MembershipConfigId("889273-2342"), false, LocalDate.now(), LocalDate.of(2026, 12, 31));
    }

    @Test
    void shouldCreateValidModel() {
        assertThat(this.membership)
            .isNotNull()
            .isInstanceOf(Membership.class);
    }

    @Test
    void shouldUpdateBuyMembershipDate() {
        LocalDate newDate = LocalDate.of(2026, 1, 1);
        this.membership.setMembershipBuyDate(newDate);
        assertThat(this.membership.getMembershipBuyDate())
            .isNotNull()
            .isEqualTo(newDate);
    }

    @Test
    void shouldUpdateExpireMembershipDate() {
        LocalDate newDate = LocalDate.of(2027, 12, 31);
        this.membership.setMembershipExpireDate(newDate);
        assertThat(this.membership.getMembershipExpireDate())
        .isNotNull()
        .isEqualTo(newDate);
    }

    @Test
    void shouldThrowExceptionWhenCreatingModel() {
        assertThatThrownBy(() -> new Membership(new MembershipId("uuid-92384"), new AccountId("82348-23948"), new MembershipConfigId("889273-2342"), false, LocalDate.of(2026, 12, 31), LocalDate.now()))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenSettingBuyMembershipDate() {
        assertThatThrownBy(() -> this.membership.setMembershipBuyDate(LocalDate.of(2027, 12, 31)))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenSettingExpireMembershipDate() {
        assertThatThrownBy(() -> this.membership.setMembershipExpireDate(LocalDate.of(2025, 12, 31)))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
