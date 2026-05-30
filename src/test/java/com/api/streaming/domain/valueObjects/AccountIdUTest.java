package com.api.streaming.domain.valueObjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AccountIdUTest {
    
    @Test
    void shouldCreateValidAccountId() {
        String id = "123456789asdf";
        AccountId accountId = new AccountId(id);

        assertThat(accountId.value()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionForBlankId() {
        assertThatThrownBy(() -> new AccountId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This is a invalid Id for creating this account.");

        assertThatThrownBy(() -> new AccountId("   "))
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new AccountId("123 456123789"))
        .isInstanceOf(IllegalArgumentException.class);
    }
}
