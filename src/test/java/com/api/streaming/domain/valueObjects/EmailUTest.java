package com.api.streaming.domain.valueObjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class EmailUTest {

    @Test
    @DisplayName("Create a new valid Email.")
    void shouldCreateValidEmail() {
        String validEmail = "test@example.com";
        Email email = new Email(validEmail);

        //Asserts
        assertThat(email.value()).isEqualTo(validEmail);
    }

    @Test
    @DisplayName("Throw Exceptcion when email param is blank.")
    void shouldThrowExceptionWhenEmailIsBlank() {
        assertThatThrownBy(() -> new Email(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email value cannot be blank or null");

        assertThatThrownBy(() -> new Email("   "))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource( strings = {
        "formato-incorrecto",
        "usuario@",
        "@dominio.com",
        "espacios en@email.com"
    })
    @DisplayName("Throw exceptions with this invalid formats for a new Email.")
    void shouldThrowExceptionsForInvalidFormats(String invalidFormat) {
        assertThatThrownBy(() -> new Email(invalidFormat))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("This string is not a valid email address.");

    }
}
