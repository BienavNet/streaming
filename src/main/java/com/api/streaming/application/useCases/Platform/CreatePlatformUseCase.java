package com.api.streaming.application.useCases.Platform;


import com.api.streaming.application.commands.CreatePlatformCommand;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePlatformUseCase {
    private final PlatformInterface platformRepository;
    private final GenerateIdInterface generateIdService;

    public PlatformId execute(CreatePlatformCommand command) {
        checkIfAlreadyExists(command.name().toLowerCase());

        PlatformId id = this.generateIdService.generatePlatformId();
        // Como no existe una plataforma, procedemos a crerla
        Platform platform = new Platform(id, command.name().toLowerCase(), command.link(), command.icon());

        // Como ya no hay mas verificaciones, hacemos el guardado en la DB
        this.platformRepository.savePlatform(platform);

        return id;
    }

    private void checkIfAlreadyExists(String name) {
        this.platformRepository.getByName(name)
            .ifPresent( platform -> {
                throw new EntityExistsException("Already exists a platform registered whit that Name.");
            });
    }
}
