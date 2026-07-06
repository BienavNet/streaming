package com.api.streaming.application.useCases.Membership;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;

@ExtendWith(MockitoExtension.class)
public class DeleteMembershipUTest {

    @Mock
    private MembershipInterface membershipInterface;

    @Mock
    private Membership membershipMock;

    @InjectMocks
    private DeleteMembershipByAccountUseCase useCase;

    private AccountId accountId;
    private MembershipId membershipId;

    @BeforeEach
    void setup() {
        this.accountId = new AccountId("uuid-acc-123");
        this.membershipId = new MembershipId("uuid-memb-999");
    }

    @Test
    void shouldDeleteMembershipSuccessfully() {
        // Given
        when(this.membershipMock.getId()).thenReturn(this.membershipId);
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));

        // When
        this.useCase.execute(this.accountId);

        // Then
        verify(this.membershipInterface, times(1)).getMembershipByAccount(this.accountId);
        verify(this.membershipInterface, times(1)).deleteMembership(this.membershipId);
    }

    @Test
    void shouldThrowExceptionWhenMembershipDoesNotExist() {
        // Given
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.accountId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Cannot delete a membership that is not registered.");

        verify(this.membershipInterface, times(1)).getMembershipByAccount(this.accountId);
        verify(this.membershipInterface, never()).deleteMembership(any());
    }
}

