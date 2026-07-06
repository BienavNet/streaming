package com.api.streaming.domain.valueObjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class MembershipConfigIdUTest {
    @Test
    void shouldCreateValidMembershipConfigId() {
        String id = "123456789asdf";
        MembershipConfigId membershipConfigId = new MembershipConfigId(id);
        
        assertThat(membershipConfigId.value()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionForBlankId() {
        assertThatThrownBy(() -> new MembershipConfigId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This is a invalid Id for creating this membership config.");

        assertThatThrownBy(() -> new MembershipConfigId("   "))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MembershipConfigId("123 456123789"))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
