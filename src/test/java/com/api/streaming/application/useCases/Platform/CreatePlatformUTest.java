package com.api.streaming.application.useCases.Platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import com.api.streaming.application.commands.CreatePlatformCommand;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityExistsException;

@ExtendWith(MockitoExtension.class)
public class CreatePlatformUTest {
    
    @Mock
    private PlatformInterface platformInterface;

    @Mock
    private GenerateIdInterface generateIdInterface;

    @InjectMocks
    private CreatePlatformUseCase createPlatformUseCase;

    private CreatePlatformCommand command;
    private PlatformId expectedId;


    @BeforeEach
    void setUp() {
        this.command = new CreatePlatformCommand("netflix", "https://www.netflix.com", "netflix-icon");

        this.expectedId = new PlatformId("uuid-23472");
    }

    @Test
    void shouldCreateAndSavePlatform_whenPlatformDoesNotExists() {
        // Arreglos (Arrange)
        when(this.platformInterface.getByName(this.command.name())).thenReturn(Optional.empty());
        when(this.generateIdInterface.generatePlatformId()).thenReturn(expectedId);

        // Actuar (Act)
        PlatformId resultId = this.createPlatformUseCase.execute(command);

        // Afirmaciones (Assert)
        assertThat(resultId)
        .isNotNull()
        .isEqualTo(this.expectedId);

        // Verificacion del objeto creado

        ArgumentCaptor<Platform> platformCaptor = ArgumentCaptor.forClass(Platform.class);
        verify(this.platformInterface).savePlatform(platformCaptor.capture());

        Platform savedPlatform = platformCaptor.getValue();
        Platform expectedPlatform = new Platform(expectedId, command.name(), command.link(), command.icon());

        assertThat(savedPlatform)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(expectedPlatform);
    }

    @Test
    void shouldThrowEntityExistsExceptionWhenPlatformAlreadyExists() {
        Platform existingPlatform = new Platform(expectedId, "netflix", "https://www.netflix.com", "netflix-icon");

        when(this.platformInterface.getByName(this.command.name())).thenReturn(Optional.of(existingPlatform));

        assertThatThrownBy(() -> this.createPlatformUseCase.execute(command))
        .isInstanceOf(EntityExistsException.class)
        .hasMessage("Already exists a platform registered whit that Name.");

        // Validaciones de seguridad
        verify(this.generateIdInterface, never()).generatePlatformId();
        verify(this.platformInterface, never()).savePlatform(any());
    }


}
