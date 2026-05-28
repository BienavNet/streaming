package com.api.streaming.application.useCases.Platform;

import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeletePlatformUseCase {
    private final PlatformInterface platformInterface;

    public void execute(PlatformId id) {

        checkIfPlatformExists(id);
        this.platformInterface.deletePlatform(id);
    }

    private void checkIfPlatformExists(PlatformId id) {
        this.platformInterface.getPlatform(id)
            .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado la plataforma con el id especificado."));
    }
}
