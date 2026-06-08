package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreatePlatformCommand;
import com.api.streaming.application.commands.UpdatePlatformCommand;
import com.api.streaming.application.useCases.Platform.CreatePlatformUseCase;
import com.api.streaming.application.useCases.Platform.DeletePlatformUseCase;
import com.api.streaming.application.useCases.Platform.GetAllPlatformsUseCase;
import com.api.streaming.application.useCases.Platform.GetPlatformUseCase;
import com.api.streaming.application.useCases.Platform.UpdatePlatformUseCase;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.platform.CreatePlatformRequest;
import com.api.streaming.infrastructure.dtos.platform.PlatformResponse;
import com.api.streaming.infrastructure.dtos.platform.UpdatePlatformRequest;
import com.api.streaming.infrastructure.mappers.PlatformMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/platform")
@RequiredArgsConstructor
public class PlatformController {
    private final CreatePlatformUseCase createPlatformUseCase;
    private final DeletePlatformUseCase deletePlatformUseCase;
    private final GetAllPlatformsUseCase getAllPlatformsUseCase;
    private final GetPlatformUseCase getPlatformUseCase;
    private final UpdatePlatformUseCase updatePlatformUseCase;
    private final PlatformMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse<PlatformResponse>> createPlatform(@RequestBody CreatePlatformRequest request) {
        CreatePlatformCommand command = this.mapper.createPlatformFromDtoToCommand(request);
        PlatformId savedPlatformId = this.createPlatformUseCase.execute(command);

        Platform savedPlatform = this.getPlatformUseCase.execute(savedPlatformId);

        GenericResponse<PlatformResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setId(savedPlatform.getId().value());
        response.setMessage("Platform created succesfully.");
        response.setData(this.mapper.fromDomainToDto(savedPlatform));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatform(@PathVariable String id) {
        PlatformId platformId = new PlatformId(id);
        this.deletePlatformUseCase.execute(platformId);
        
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("")
    public ResponseEntity<GenericResponse<List<PlatformResponse>>> getPlatforms() {
        List<Platform> platforms = this.getAllPlatformsUseCase.execute();

        GenericResponse<List<PlatformResponse>> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainListToDtoList(platforms));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<PlatformResponse>> getPlatform(@PathVariable String id) {
        PlatformId platformId = new PlatformId(id);
        Platform platform = this.getPlatformUseCase.execute(platformId);

        GenericResponse<PlatformResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainToDto(platform));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<PlatformResponse>> updatePlatform(@PathVariable String id, @RequestBody UpdatePlatformRequest platformRequest) {
        UpdatePlatformCommand command = new UpdatePlatformCommand(new PlatformId(id), platformRequest.getName(), platformRequest.getLink(), platformRequest.getIcon());

        PlatformId updatedPlatformId = this.updatePlatformUseCase.execute(command);

        Platform updatedPlatform = this.getPlatformUseCase.execute(updatedPlatformId);

        GenericResponse<PlatformResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainToDto(updatedPlatform));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    
    

}
