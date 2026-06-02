package com.api.streaming.application.useCases.Platform;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DeletePlatformUTest {
    
    @Mock
    private PlatformInterface platformInterface;

    @InjectMocks
    private DeletePlatformUseCase deletePlatformUseCase;

    private PlatformId expectedId;
    private Platform expectedPlatform;

    @BeforeEach
    void setUp() {
        this.expectedId = new PlatformId("uuid-92342");
        this.expectedPlatform = new Platform(expectedId, "Netflix", "https://www.netflix.com", "Netflix-icon");
    }

    @Test
    void shouldCheckAndDeletePlatformWhenPlatformExists() {
        when(this.platformInterface.getPlatform(this.expectedId)).thenReturn(Optional.of(this.expectedPlatform));

        this.deletePlatformUseCase.execute(expectedId);

        verify(this.platformInterface, times(1)).getPlatform(expectedId);
        verify(this.platformInterface, times(1)).deletePlatform(expectedId);

    }

    @Test
    void shouldNotDeletePlatformWhenPlatformDoesNotExists() {
        when(this.platformInterface.getPlatform(this.expectedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deletePlatformUseCase.execute(this.expectedId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Platform does not exist.");

        verify(this.platformInterface, times(1)).getPlatform(this.expectedId);
        verify(this.platformInterface, never()).deletePlatform(expectedId);
    }
}
