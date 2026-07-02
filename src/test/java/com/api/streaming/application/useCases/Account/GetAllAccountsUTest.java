package com.api.streaming.application.useCases.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

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

@ExtendWith(MockitoExtension.class)
public class GetAllAccountsUTest {
    
    @Mock
    private AccountQueriesInterface accountQueriesInterface;

    @InjectMocks
    private GetAllAccountsUseCase getAllAccountsUseCase;


    @Test
    void shouldReturnAllAccountsSuccessfully() {

        Platform platform = new Platform(new PlatformId("uuid-123"), "netflix", "https://www.netflix.com", "netflix-icon");
        
        Payment payment = new Payment(new PaymentId("uuid-456"), "4545454545454545", 10, 2026, "123", "Banco de Colombia.");

        MembershipDetailsQuery membershipDetailsQuery = new MembershipDetailsQuery(new MembershipId("uuid-234982"), new MembershipConfig(new MembershipConfigId("uuid-289218"), platform.getId(), "plan 1", 5));
        
        AccountDetailsQuery account1 = new AccountDetailsQuery(new AccountId("uuid-12312"), new Email("email@test.com"), "122344521", false, LocalDate.MIN, LocalDate.MAX, LocalDate.now(), platform, payment, AccountStatus.ACTIVE, membershipDetailsQuery);
        
        AccountDetailsQuery account2 = new AccountDetailsQuery(new AccountId("uuid-12314"), new Email("email@test.com"), "122344521", false, LocalDate.MIN, LocalDate.MAX, LocalDate.now(), platform, payment, AccountStatus.ACTIVE, membershipDetailsQuery);
        
        List<AccountDetailsQuery> mock = List.of(account1, account2);
        
        when(this.accountQueriesInterface.getAccountsDetails()).thenReturn(mock);

        assertThat(this.getAllAccountsUseCase.execute())
        .isNotNull()
        .hasSize(2)
        .isEqualTo(mock);


    }
}
