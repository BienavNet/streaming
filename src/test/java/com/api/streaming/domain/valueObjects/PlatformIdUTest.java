package com.api.streaming.domain.valueObjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class PlatformIdUTest {
    @Test
    void shouldCreateValidPlatformId() {
        String id = "123456789asdf";
        PlatformId PlatformId = new PlatformId(id);

        assertThat(PlatformId.value()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionForBlankId() {
        assertThatThrownBy(() -> new PlatformId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This is a invalid Id for creating this platform.");

        assertThatThrownBy(() -> new PlatformId("   "))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new PlatformId("123 456123789"))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
