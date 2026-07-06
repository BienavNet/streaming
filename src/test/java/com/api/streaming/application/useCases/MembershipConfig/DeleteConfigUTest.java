package com.api.streaming.application.useCases.MembershipConfig;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DeleteConfigUTest {

    @Mock
    private MembershipConfigInterface membershipConfigInterface;

    @InjectMocks
    private DeleteMembershipConfigUseCase useCase;

    private MembershipConfigId configId;
    private MembershipConfig membershipConfig;

    @BeforeEach
    void setup() {
        this.configId = new MembershipConfigId("uuid-092309");
        PlatformId platformId = new PlatformId("uuid-3298482");
        this.membershipConfig = new MembershipConfig(this.configId, platformId, "basic", 10);
    }

    @Test
    void shouldDeleteConfigSuccessfully() {
        // Given
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.of(this.membershipConfig));

        // When
        this.useCase.execute(this.configId);

        // Then
        verify(this.membershipConfigInterface, times(1)).getMembershipConfig(this.configId);
        verify(this.membershipConfigInterface, times(1)).deleteMembershipConfig(this.configId);
    }

    @Test
    void shouldThrowExceptionWhenConfigDoesNotExist() {
        // Given
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.configId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Membership Config does not exist.");

        verify(this.membershipConfigInterface, times(1)).getMembershipConfig(this.configId);
        verify(this.membershipConfigInterface, never()).deleteMembershipConfig(any());
    }
}
