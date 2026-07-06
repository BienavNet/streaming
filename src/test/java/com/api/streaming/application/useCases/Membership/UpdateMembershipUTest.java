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
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.mappers.MembershipMapper;

@ExtendWith(MockitoExtension.class)
public class UpdateMembershipUTest {

    @Mock
    private MembershipInterface membershipInterface;

    @Mock
    private MembershipMapper mapper;

    @Mock
    private Membership membershipMock; 

    @InjectMocks
    private UpdateMembershipUseCase useCase;

    private UpdateMembershipCommand command;
    private MembershipId membershipId;

    @BeforeEach
    void setup() {
        this.membershipId = new MembershipId("uuid-memb-123");
        MembershipConfigId configId = new MembershipConfigId("uuid-cfg-456");

        this.command = new UpdateMembershipCommand(
            this.membershipId,
            configId,
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(3)
        );

        
    }

    @Test
    void shouldUpdateMembershipSuccessfully() {
        // Given
        when(this.membershipMock.getId()).thenReturn(this.membershipId);
        when(this.membershipInterface.getMembership(this.membershipId))
            .thenReturn(Optional.of(this.membershipMock));

        // When
        MembershipId resultId = this.useCase.execute(this.command);

        // Then
        assertThat(resultId).isEqualTo(this.membershipId);

        verify(this.membershipInterface, times(1)).getMembership(this.membershipId);
        
        verify(this.mapper, times(1)).updateMembershipFromCommand(this.command, this.membershipMock);
        
        verify(this.membershipInterface, times(1)).saveMembership(this.membershipMock);
    }

    @Test
    void shouldThrowExceptionWhenMembershipToUpdateDoesNotExist() {
        // Given
        when(this.membershipInterface.getMembership(this.membershipId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.command))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Cannot update membership because that does not exist.");

        verify(this.membershipInterface, times(1)).getMembership(this.membershipId);
        verify(this.mapper, never()).updateMembershipFromCommand(any(), any());
        verify(this.membershipInterface, never()).saveMembership(any());
    }
}
