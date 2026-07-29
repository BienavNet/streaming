package com.api.streaming.application.useCases.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapper;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapper;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapper;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountUTest {
    
    @Mock
    private AccountInterface accountInterface;
    @Mock
    private PaymentInterface paymentInterface;
    @Mock
    private PlatformInterface platformInterface;
    @Mock
    private MembershipInterface membershipInterface;

    @Spy
    private PlatformMapper platformMapper = new PlatformMapperImpl();
    @Spy
    private PaymentMapper paymentMapper = new PaymentMapperImpl();
    
    @Spy
    private AccountMapper accountMapper = new AccountMapperImpl();

    @InjectMocks
    private UpdateAccountUseCase updateAccountUseCase;


    private AccountId expectedId;
    private UpdateAccountCommand command;
    private Account account;
    private Platform platform;
    private Payment payment;

    @BeforeEach
    void setup() {
        this.expectedId = new AccountId("uuid-92834");
        
        this.platform = new Platform(new PlatformId("uuid-123"), "netflix", "https://www.netflix.com", "netflix-icon");

        this.payment = new Payment(new PaymentId("uuid-456"), "4545454545454545", 10, 2026, "123", "Banco de Colombia.");

        this.command = new UpdateAccountCommand(expectedId, new Email("testing@gmail.com"), "123498723", this.platform.getId(), this.payment.getId());

        this.account = new Account(this.expectedId, new Email("test@test.com"), "123456789", LocalDate.now(), platform.getId(), payment.getId());

        org.springframework.test.util.ReflectionTestUtils.setField(accountMapper, "platformMapper", platformMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(accountMapper, "paymentMapper", paymentMapper);
    }

    @Test
    void shouldUpdateAccountSuccesfully() {
        when(this.paymentInterface.getPayment(this.payment.getId())).thenReturn(Optional.of(this.payment));
        when(this.platformInterface.getPlatform(this.platform.getId())).thenReturn(Optional.of(this.platform));
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.of(this.account));
        when(this.membershipInterface.getMembershipByAccount(this.expectedId)).thenReturn(Optional.empty());

        AccountId resultId = this.updateAccountUseCase.execute(command);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(this.accountInterface, times(1)).saveAccount(accountCaptor.capture());

        Account expectedAccount = accountCaptor.getValue();

        assertThat(resultId)
        .isNotNull()
        .isEqualTo(this.expectedId);

        assertThat(expectedAccount)
        .isNotNull()
        .extracting(
            Account::getEmail, 
            Account::getPassword,
            Account::getPlatform,
            Account::getPayment
        )
        .containsExactly(
            command.email(),
            command.password(),
            command.platform(),
            command.payment()
        );
        // .isEqualTo(this.command.password())

        verify(this.accountMapper, times(1)).updateAccountFromCommand(command, expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.updateAccountUseCase.execute(this.command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Account you're trying to update doesn't exist.");

        verify(this.accountInterface, never()).saveAccount(any());
        verify(this.accountMapper, never()).updateAccountFromCommand(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenPaymentDoesNotExist() {
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.of(this.account));
        when(this.paymentInterface.getPayment(this.payment.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.updateAccountUseCase.execute(this.command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("New payment is not registered yet.");

        verify(this.accountInterface, never()).saveAccount(any());
        verify(this.platformInterface, never()).getPlatform(this.platform.getId());
        verify(this.accountMapper, never()).updateAccountFromCommand(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenPlatformDoesNotExist() {
        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.of(this.account));
        when(this.paymentInterface.getPayment(this.payment.getId())).thenReturn(Optional.of(this.payment));
        when(this.platformInterface.getPlatform(this.platform.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.updateAccountUseCase.execute(this.command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("New platform is not registered yet.");

        verify(this.accountInterface, never()).saveAccount(any());
        verify(this.accountMapper, never()).updateAccountFromCommand(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsUsedByOtherAccount() {

        Account expectedAccount = new Account(expectedId, new Email("testing@test.com"), "123456789", LocalDate.now(), platform.getId(), payment.getId());
        List<Account> expectedList = List.of(expectedAccount, expectedAccount);

        when(this.accountInterface.getAccount(this.expectedId)).thenReturn(Optional.of(this.account));
        when(this.paymentInterface.getPayment(this.payment.getId())).thenReturn(Optional.of(this.payment));
        when(this.platformInterface.getPlatform(this.platform.getId())).thenReturn(Optional.of(this.platform));
        when(this.accountInterface.getAccountByEmailAndPlatform(this.command.email(), expectedAccount.getPlatform())).thenReturn(expectedList);

        assertThatThrownBy(() -> this.updateAccountUseCase.execute(this.command))
        .isInstanceOf(EntityExistsException.class)
        .hasMessage("There is already an account registered with this email for this platform.");

        verify(this.accountInterface, never()).saveAccount(any());
        verify(this.accountMapper, never()).updateAccountFromCommand(any(), any());
    }
}
