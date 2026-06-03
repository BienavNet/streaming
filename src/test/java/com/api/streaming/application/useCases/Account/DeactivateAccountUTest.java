package com.api.streaming.application.useCases.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
public class DeactivateAccountUTest {
    
    @Mock
    private AccountInterface accountInterface;

    @InjectMocks
    private DeactivateAccountUseCase deactivateAccountUseCase;


    private AccountId expectedId;
    private Account expectedAccount;

    @BeforeEach
    void setup() {
        this.expectedId = new AccountId("uuid-4565");
        this.expectedAccount = new Account(this.expectedId, new Email("email@test.com"), "123456789", false, LocalDate.MIN, LocalDate.MAX, LocalDate.now(), new PlatformId("uuid-123"), new PaymentId("uuid-789"), AccountStatus.ACTIVE);
    }

    @Test
    void shouldDeactivateAccountSuccesfully() {
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.of(this.expectedAccount));

        this.deactivateAccountUseCase.execute(this.expectedId);
        
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(this.accountInterface, times(1)).saveAccount(accountCaptor.capture());

        Account resultAccount = accountCaptor.getValue();

        assertThat(resultAccount)
        .isNotNull()
        .extracting(Account::getStatus)
        .isEqualTo(AccountStatus.DEACTIVE);

        verify(this.accountInterface, times(1)).getAccount(this.expectedId);
        verify(this.accountInterface, times(1)).saveAccount(accountCaptor.capture());
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deactivateAccountUseCase.execute(this.expectedId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Account that you want deactivate doesn't exist.");
    }

    @Test
    void shouldThrowExceptionWhenAccountIsAlreadyDeactivated() {
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.of(this.expectedAccount));

        this.expectedAccount.setStatus(AccountStatus.DEACTIVE);

        assertThatThrownBy(() -> this.deactivateAccountUseCase.execute(this.expectedId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Account is already deactivated.");
    }
}
