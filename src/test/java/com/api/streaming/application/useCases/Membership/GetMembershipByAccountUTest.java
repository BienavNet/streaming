package com.api.streaming.application.useCases.Membership;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;

@ExtendWith(MockitoExtension.class)
public class GetMembershipByAccountUTest {

    @Mock
    private MembershipDetailsInterface membershipDetailsInterface;

    @Mock
    private MembershipConfig membershipConfigMock; 

    @InjectMocks
    private GetMembershipByAccountIdUseCase useCase;

    private AccountId accountId;
    private MembershipDetailsQuery sampleQuery;

    @BeforeEach
    void setup() {
        this.accountId = new AccountId("uuid-acc-123");
        MembershipId membershipId = new MembershipId("uuid-memb-999");

        this.sampleQuery = new MembershipDetailsQuery(
            membershipId,
            this.membershipConfigMock,
            true,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
        );
    }

    @Test
    void shouldReturnMembershipDetailsSuccessfully() {
        // Given
        when(this.membershipDetailsInterface.getMembershipDetailsByAccount(this.accountId))
            .thenReturn(Optional.of(this.sampleQuery));

        // When
        MembershipDetailsQuery result = this.useCase.execute(this.accountId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(this.sampleQuery.id());
        assertThat(result.isFreeTrial()).isTrue();

        verify(this.membershipDetailsInterface, times(1)).getMembershipDetailsByAccount(this.accountId);
    }

    @Test
    void shouldThrowExceptionWhenMembershipDetailsDoNotExist() {
        // Given
        when(this.membershipDetailsInterface.getMembershipDetailsByAccount(this.accountId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.accountId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("No account is registered to query this membership.");

        verify(this.membershipDetailsInterface, times(1)).getMembershipDetailsByAccount(this.accountId);
    }
}
