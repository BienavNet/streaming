package com.api.streaming.domain.valueObjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class MembershipIdUTest {
    @Test
    void shouldCreateValidMembershipId() {
        String id = "123456789asdf";
        MembershipId membershipId = new MembershipId(id);

        assertThat(membershipId.value()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionForBlankId() {
        assertThatThrownBy(() -> new MembershipId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This is a invalid Id for creating this membership.");

        assertThatThrownBy(() -> new MembershipId("   "))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MembershipId("123 456123789"))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
