package com.api.streaming.application.useCases.Platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
public class GetPlatformUTest {

    @Mock
    private PlatformInterface platformInterface;

    @InjectMocks
    private GetPlatformUseCase getPlatformUseCase;

    private PlatformId expectedId;
    private Platform expectedPlatform;

    @BeforeEach
    void setUp() {
        this.expectedId = new PlatformId("uuid-82348");
        this.expectedPlatform = new Platform(expectedId, "netflix", "https://www.netflix.com", "netflix-icon");
    }

    @Test
    void shouldObtainPlatfromWhenPlatformExists() {
        when(this.platformInterface.getPlatform(this.expectedId)).thenReturn(Optional.of(this.expectedPlatform));

        assertThat(this.getPlatformUseCase.execute(this.expectedId))
        .isNotNull()
        .isInstanceOf(Platform.class)
        .isEqualTo(this.expectedPlatform);

        verify(this.platformInterface, times(1)).getPlatform(this.expectedId);
    }

    @Test
    void shouldNotObtainPlatformWhenThisDoesNotExist() {
        when(this.platformInterface.getPlatform(this.expectedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.getPlatformUseCase.execute(this.expectedId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Platform with that ID not found.");
    }
    
}
