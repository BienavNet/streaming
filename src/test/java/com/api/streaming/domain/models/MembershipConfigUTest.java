package com.api.streaming.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

public class MembershipConfigUTest {
    
    private MembershipConfig config;

    @BeforeEach
    void setup() {
        this.config = new MembershipConfig(new MembershipConfigId("uuid-98234"), new PlatformId("uuid-239488"), "basic", 3);
    }

    @Test
    void shouldCreateValidModel() {
        assertThat(this.config).isNotNull().isInstanceOf(MembershipConfig.class);
    }

    @Test
    void shouldThrowExceptionWhenProfilesIsZeroOrNegative() {
        assertThatThrownBy(() -> this.config.setProfiles(-1))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MembershipConfig(new MembershipConfigId("uuid-98234"), new PlatformId("uuid-239488"), "basic", 0))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
