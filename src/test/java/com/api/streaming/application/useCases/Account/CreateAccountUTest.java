package com.api.streaming.application.useCases.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUTest {
    
    @Mock
    private AccountInterface accountInterface;
    @Mock
    private PaymentInterface paymentInterface;
    @Mock
    private PlatformInterface platformInterface;
    @Mock
    private GenerateIdInterface generateIdInterface;

    @InjectMocks
    private CreateAccountUseCase createAccountUseCase;

    private CreateAccountCommand command;
    private AccountId expectedId;
    private Platform platform;
    private Payment payment; 

    @BeforeEach
    void setup() {
        this.expectedId = new AccountId("uuid-92834");
        
        this.platform = new Platform(new PlatformId("uuid-123"), "netflix", "https://www.netflix.com", "netflix-icon");
        this.payment = new Payment(new PaymentId("uuid-456"), "4545454545454545", 10, 2026, "123", "Banco de Colombia.");
        this.command = new CreateAccountCommand(new Email("test@test.com"), "123456789", platform.getId(), payment.getId());
    }

    @Test
    void shouldCreateAndSaveNewAccountSuccessfully() {
        when(this.paymentInterface.getPayment(this.command.payment())).thenReturn(Optional.of(this.payment));
        when(this.platformInterface.getPlatform(this.command.platform())).thenReturn(Optional.of(this.platform));
        when(this.generateIdInterface.generateAccountId()).thenReturn(this.expectedId);

        AccountId resultId = this.createAccountUseCase.execute(command);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(this.accountInterface, times(1)).saveAccount(accountCaptor.capture());

        Account savedAccount = accountCaptor.getValue();
        Account expectedAccount = new Account(resultId, command.email(), command.password(), LocalDate.now(), command.platform(), command.payment());

        assertThat(resultId)
        .isNotNull()
        .isEqualTo(this.expectedId);

        assertThat(savedAccount)
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("registeredAt")
        .isEqualTo(expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {
        when(this.paymentInterface.getPayment(this.command.payment())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.createAccountUseCase.execute(this.command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Payment ID does not exists.");

        verify(this.paymentInterface, times(1)).getPayment(this.command.payment());
        verify(this.platformInterface, never()).getPlatform(this.command.platform());
        verify(this.accountInterface, never()).saveAccount(any());

    }

    @Test
    void shouldThrowExceptionWhenPlatformNotFound() {
        when(this.paymentInterface.getPayment(this.command.payment())).thenReturn(Optional.of(this.payment));
        when(this.platformInterface.getPlatform(this.command.platform())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.createAccountUseCase.execute(this.command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Platform ID doesn't exists.");

        verify(this.paymentInterface, times(1)).getPayment(this.command.payment());
        verify(this.platformInterface, times(1)).getPlatform(this.command.platform());
        verify(this.accountInterface, never()).saveAccount(any());

    }

    @Test
    void shouldThrowExceptionWhenEmailIsAlreadyRegisteredWithAPlatform() {

        Account expectedAccount = new Account(expectedId, new Email("test@test.com"), "123456789", LocalDate.now(), platform.getId(), payment.getId());
        List<Account> expectedList = List.of(expectedAccount, expectedAccount);


        when(this.paymentInterface.getPayment(this.command.payment())).thenReturn(Optional.of(this.payment));
        when(this.platformInterface.getPlatform(this.command.platform())).thenReturn(Optional.of(this.platform));
        when(this.accountInterface.getAccountByEmailAndPlatform(expectedAccount.getEmail(), expectedAccount.getPlatform())).thenReturn(expectedList);

        assertThatThrownBy(() -> this.createAccountUseCase.execute(this.command))
        .isInstanceOf(EntityExistsException.class)
        .hasMessage("There is already an account registered with this email for this platform.");

        verify(this.paymentInterface, times(1)).getPayment(this.command.payment());
        verify(this.platformInterface, times(1)).getPlatform(this.command.platform());
        verify(this.accountInterface, never()).saveAccount(any());

    }
}
