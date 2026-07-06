package com.api.streaming.application.useCases.MembershipConfig;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.CreateMembershipConfigCommand;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CreateConfigUTest {
    
    @Mock
    private MembershipConfigInterface membershipConfigInterface;

    @Mock
    private GenerateIdInterface generateIdInterface;

    @Mock
    private PlatformInterface platformInterface;

    @InjectMocks
    private CreateMembershipConfigUseCase useCase;


    private MembershipConfigId configId;
    private PlatformId platformId;
    private CreateMembershipConfigCommand command;
    private Platform platform;

    @BeforeEach
    void setup() {
        this.configId = new MembershipConfigId("uuid-092309");
        this.platformId = new PlatformId("uuid-3298482");
        this.command = new CreateMembershipConfigCommand(platformId, "basic", 10);
        this.platform = new Platform(platformId, "netflix", "http://www.netflix.com", "netflix-icon");
    }

    @Test
    void shouldCreateAndSaveModel() {
        when(this.platformInterface.getPlatform(this.command.platform())).thenReturn(Optional.of(this.platform));
        when(this.generateIdInterface.generateMembershipConfigId()).thenReturn(configId);

        MembershipConfigId resultId = this.useCase.execute(command);

        ArgumentCaptor<MembershipConfig> configCaptor = ArgumentCaptor.forClass(MembershipConfig.class);
        verify(this.membershipConfigInterface, times(1)).saveMembershipConfig(configCaptor.capture());

        MembershipConfig savedConfig = configCaptor.getValue();
        MembershipConfig expectedConfig = new MembershipConfig(resultId, platformId, "basic", 10);

        assertThat(resultId)
        .isNotNull()
        .isEqualTo(this.configId);

        assertThat(savedConfig)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(expectedConfig);
    }

    @Test
    void shouldThrowExceptionWhenPlatformNotFound() {
        when(this.platformInterface.getPlatform(this.command.platform())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.useCase.execute(this.command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("This platform does not exists.");

        verify(this.platformInterface, times(1)).getPlatform(this.command.platform());
        verify(this.generateIdInterface, never()).generateMembershipConfigId();
        verify(this.membershipConfigInterface, never()).saveMembershipConfig(any());
    }
}
