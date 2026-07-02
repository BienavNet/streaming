package com.api.streaming.application.useCases.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GetAccountFromIdUTest {
    
    @Mock
    private AccountQueriesInterface accountInterface;

    @InjectMocks
    private GetAccountFromIdUseCase getAccountFromIdUseCase;


    private AccountId accountId;
    private AccountDetailsQuery expectedAccount;
    private Platform platform;
    private Payment payment; 
    private MembershipDetailsQuery membershipDetailsQuery;

    @BeforeEach
    void setup() {
        this.accountId = new AccountId("uuid-2134");
        this.platform = new Platform(new PlatformId("uuid-123"), "netflix", "https://www.netflix.com", "netflix-icon");
        this.payment = new Payment(new PaymentId("uuid-456"), "4545454545454545", 10, 2026, "123", "Banco de Colombia.");
        this.membershipDetailsQuery = new MembershipDetailsQuery(new MembershipId("uuid-234982"), new MembershipConfig(new MembershipConfigId("uuid-289218"), this.platform.getId(), "plan 1", 5), false, LocalDate.MIN, LocalDate.MAX);
        this.expectedAccount = new AccountDetailsQuery(accountId, new Email("email@test.com"), "122344521", LocalDate.now(), this.platform, this.payment, AccountStatus.ACTIVE, this.membershipDetailsQuery);
    }

    @Test
    void shouldGetInfoSuccessfully() {
        when(this.accountInterface.getAccountDetailById(this.accountId)).thenReturn(Optional.of(this.expectedAccount));

        AccountDetailsQuery resultAccount = this.getAccountFromIdUseCase.execute(accountId);

        assertThat(resultAccount)
        .isNotNull()
        .isEqualTo(this.expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(this.accountInterface.getAccountDetailById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.getAccountFromIdUseCase.execute(this.accountId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("This account doesn't exist.");

        
    }
}
