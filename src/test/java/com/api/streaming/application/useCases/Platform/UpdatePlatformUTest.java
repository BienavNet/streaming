package com.api.streaming.application.useCases.Platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.UpdatePlatformCommand;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.PlatformMapper;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UpdatePlatformUTest {
    
    @Mock
    private PlatformInterface platformInterface;

    @InjectMocks
    private UpdatePlatformUseCase updatePlatformUseCase;

    @Spy
    private PlatformMapper mapper = new PlatformMapperImpl();

    private PlatformId expectedId;
    private Platform expectedPlatform;
    private UpdatePlatformCommand command;

    @BeforeEach
    void setUp() {
        this.expectedId = new PlatformId("uuid-93242");
        this.expectedPlatform = new Platform(expectedId, "netflixx", "https://www.netflixx.com", "netflixx-icon");
        this.command = new UpdatePlatformCommand(expectedId, "netflix", "https://www.netflix.com", "netflix-icon");
    }

    @Test
    void shouldUpdatePlatformWhenThisExists() {
        when(this.platformInterface.getPlatform(expectedId)).thenReturn(Optional.of(this.expectedPlatform));

        when(this.platformInterface.savePlatform(this.expectedPlatform)).thenReturn(this.expectedPlatform);


        Platform result = this.updatePlatformUseCase.execute(this.command);

        assertThat(result.getName())
        .isEqualTo("netflix");

        assertThat(result.getLink())
        .isEqualTo("https://www.netflix.com");

        assertThat(result.getIcon())
        .isEqualTo("netflix-icon");

        verify(this.platformInterface, times(1)).getPlatform(this.expectedId);
        verify(this.mapper, times(1)).UpdatePlatformFromCommand(command, result);
        verify(this.platformInterface, times(1)).savePlatform(this.expectedPlatform);
    }

    @Test
    void shouldThrowExceptionWhenPlatformToUpdateDoesNotExist() {
        when(this.platformInterface.getPlatform(this.expectedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.updatePlatformUseCase.execute(command))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Platform that you want to update, doesn't exist.");

        verify(this.platformInterface, times(1)).getPlatform(this.expectedId);
        verify(this.platformInterface, never()).savePlatform(this.expectedPlatform);
        verifyNoInteractions(this.mapper);
        // verify(this.mapper, times(0)).UpdatePlatformFromCommand(command, expectedPlatform);
    }
}
