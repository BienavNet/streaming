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
    public ResponseEntity<GenericResponse> createPlatform(@RequestBody CreatePlatformRequest request) {
        CreatePlatformCommand command = this.mapper.createPlatformFromDtoToCommand(request);
        String id = this.createPlatformUseCase.execute(command).value();

        GenericResponse response = GenericResponse.builder().id(id).status(HttpStatus.CREATED.value()).build();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deletePlatform(@PathVariable String id) {
        PlatformId platformId = new PlatformId(id);
        this.deletePlatformUseCase.execute(platformId);

        GenericResponse response = GenericResponse
                                        .builder()
                                        .id(id)
                                        .status(HttpStatus.NO_CONTENT.value())
                                        .build();
        
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<PlatformResponse>> getPlatforms() {
        List<Platform> platforms = this.getAllPlatformsUseCase.execute();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.mapper.fromDomainListToDtoList(platforms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformResponse> getPlatform(@PathVariable String id) {
        PlatformId platformId = new PlatformId(id);
        Platform platform = this.getPlatformUseCase.execute(platformId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.mapper.fromDomainToDto(platform));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updatePlatform(@PathVariable String id, @RequestBody UpdatePlatformRequest platformRequest) {
        UpdatePlatformCommand command = new UpdatePlatformCommand(new PlatformId(id), platformRequest.getName(), platformRequest.getLink(), platformRequest.getIcon());

        this.updatePlatformUseCase.execute(command);

        GenericResponse response = GenericResponse.builder().id(id).status(HttpStatus.OK.value()).build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    
    

}
