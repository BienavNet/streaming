package com.api.streaming.application.useCases.Platform;

import com.api.streaming.application.commands.UpdatePlatformCommand;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.PlatformMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePlatformUseCase {
    private final PlatformInterface platformInterface;
    private final PlatformMapper mapper;

    public Platform execute(UpdatePlatformCommand command) {
        Platform currentPlatform = this.checkIfPlatformExists(command.id());
        this.mapper.UpdatePlatformFromCommand(command, currentPlatform);

        Platform platformUpdated = this.platformInterface.savePlatform(currentPlatform);

        return platformUpdated;

    }

    private Platform checkIfPlatformExists(PlatformId id) {
        return this.platformInterface.getPlatform(id)
            .orElseThrow(() -> new EntityNotFoundException("Platform that you want to update, doesn't exist."));
    }
}
