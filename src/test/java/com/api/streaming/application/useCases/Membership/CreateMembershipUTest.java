package com.api.streaming.application.useCases.Membership;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.CreateMembershipCommand;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

@ExtendWith(MockitoExtension.class)
public class CreateMembershipUTest {

    @Mock
    private MembershipInterface membershipInterface;
    
    @Mock
    private GenerateIdInterface generateIdInterface;
    
    @Mock
    private AccountInterface accountInterface;
    
    @Mock
    private MembershipConfigInterface membershipConfigInterface;

    @InjectMocks
    private CreateMembershipUseCase useCase;

    // Fixtures / Test Data
    private CreateMembershipCommand command;
    private AccountId accountId;
    private MembershipConfigId configId;
    private PlatformId platformId;
    private PlatformId differentPlatformId;
    private MembershipId newMembershipId;
    private Account account;
    private MembershipConfig membershipConfig;
    private Membership membership;

    @BeforeEach
    void setup() {
        this.accountId = new AccountId("uuid-acc-123");
        this.configId = new MembershipConfigId("uuid-cfg-456");
        this.platformId = new PlatformId("uuid-plat-789");
        this.differentPlatformId = new PlatformId("uuid-plat-000");
        this.newMembershipId = new MembershipId("uuid-memb-999");

        // Inicializar el comando con LocalDates
        this.command = new CreateMembershipCommand(
            this.accountId,
            this.configId,
            true,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
        );

        // Inicializar entidades mockeadas básicas
        // Asumiendo un constructor para Account que reciba id y platform (puedes mockearlo si no tiene constructor público fácil)
        this.account = new Account(accountId, new Email("email@gmail.com"), "123456789", LocalDate.now(), this.platformId, new PaymentId("uuid-98234"));
        this.membershipConfig = new MembershipConfig(this.configId, this.platformId, "premium", 15);
        this.membership = new Membership(
            this.newMembershipId,
            this.accountId,
            this.configId,
            command.isFreeTrial(),
            command.membershipBuyDate(),
            command.membershipExpireDate()
        );
    }

    @Test
    void shouldCreateMembershipSuccessfully() {
        // Given
        when(this.accountInterface.getAccount(this.accountId))
            .thenReturn(Optional.of(this.account));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.empty());
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.of(this.membershipConfig));
        when(this.generateIdInterface.generateMembershipId())
            .thenReturn(this.newMembershipId);

        // When
        MembershipId resultId = this.useCase.execute(this.command);

        // Then
        assertThat(resultId).isEqualTo(this.newMembershipId);
        
        verify(this.accountInterface, times(2)).getAccount(this.accountId);
        verify(this.membershipInterface, times(1)).getMembershipByAccount(this.accountId);
        verify(this.membershipConfigInterface, times(1)).getMembershipConfig(this.configId);
        verify(this.generateIdInterface, times(1)).generateMembershipId();
        verify(this.membershipInterface, times(1)).saveMembership(any(Membership.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        // Given
        when(this.accountInterface.getAccount(this.accountId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.command))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("This account does not exists.");

        verify(this.accountInterface, times(1)).getAccount(this.accountId);
        verify(this.membershipInterface, never()).getMembershipByAccount(any());
        verify(this.membershipInterface, never()).saveMembership(any());
    }

    @Test
    void shouldThrowExceptionWhenAccountAlreadyHasMembership() {
        // Given
        when(this.accountInterface.getAccount(this.accountId))
            .thenReturn(Optional.of(this.account));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membership));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.command))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("This account already has a membership registered.");

        verify(this.accountInterface, times(1)).getAccount(this.accountId);
        verify(this.membershipInterface, times(1)).getMembershipByAccount(this.accountId);
        verify(this.membershipConfigInterface, never()).getMembershipConfig(any());
        verify(this.membershipInterface, never()).saveMembership(any());
    }

    @Test
    void shouldThrowExceptionWhenMembershipConfigDoesNotExist() {
        // Given
        when(this.accountInterface.getAccount(this.accountId))
            .thenReturn(Optional.of(this.account));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.empty());
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.command))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("This membership config does not exists, so couldn't attach to this membership.");

        verify(this.membershipConfigInterface, times(1)).getMembershipConfig(this.configId);
        verify(this.membershipInterface, never()).saveMembership(any());
    }

    @Test
    void shouldThrowExceptionWhenPlatformsDoNotMatch() {
        // Given
        MembershipConfig mismatchedConfig = new MembershipConfig(this.configId, this.differentPlatformId, "premium", 15);


        when(this.accountInterface.getAccount(this.accountId))
            .thenReturn(Optional.of(this.account));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.empty());
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.of(mismatchedConfig));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Your platform account must be match with membership platform.");

        verify(this.membershipInterface, never()).saveMembership(any());
    }
}
