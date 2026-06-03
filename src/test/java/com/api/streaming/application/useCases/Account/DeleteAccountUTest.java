package com.api.streaming.application.useCases.Account;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountUTest {
    
    @Mock
    private AccountInterface accountInterface;

    @InjectMocks
    private DeleteAccountUseCase deleteAccountUseCase;

    private AccountId accountId;
    private Account expectedAccount;


    @BeforeEach
    void setup() {
        this.accountId  = new AccountId("uuid-4654");
        this.expectedAccount = new Account(this.accountId, new Email("email@test.com"), "123456789", false, LocalDate.MIN, LocalDate.MAX, LocalDate.now(), new PlatformId("uuid-123"), new PaymentId("uuid-789"), AccountStatus.ACTIVE);
    }

    @Test
    void shouldDeleteAccountSuccessfully() {
        when(this.accountInterface.getAccount(this.accountId)).thenReturn(Optional.of(this.expectedAccount));

        this.deleteAccountUseCase.execute(this.accountId);

        verify(this.accountInterface, times(1)).getAccount(this.accountId);
        verify(this.accountInterface, times(1)).deleteAccount(this.accountId);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(this.accountInterface.getAccount(this.accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deleteAccountUseCase.execute(this.accountId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Account that you're trying to delete doesn't exist.");
    }
}
