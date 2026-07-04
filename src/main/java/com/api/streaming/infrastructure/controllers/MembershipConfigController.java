package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreateMembershipConfigCommand;
import com.api.streaming.application.useCases.MembershipConfig.CreateMembershipConfigUseCase;
import com.api.streaming.application.useCases.MembershipConfig.DeleteMembershipConfigUseCase;
import com.api.streaming.application.useCases.MembershipConfig.GetAllMembershipConfigByPlatformUseCase;
import com.api.streaming.application.useCases.MembershipConfig.GetMembershipConfigUseCase;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.membershipConfig.MembershipConfigResponse;
import com.api.streaming.infrastructure.mappers.MembershipConfigMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/membership/config")
@RequiredArgsConstructor
public class MembershipConfigController {
    
    private final CreateMembershipConfigUseCase createMembershipConfigUseCase;
    private final DeleteMembershipConfigUseCase deleteMembershipConfigUseCase;
    private final GetAllMembershipConfigByPlatformUseCase getAllMembershipConfigByPlatformUseCase;
    private final GetMembershipConfigUseCase getMembershipConfigUseCase;
    private final MembershipConfigMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse<String>> saveMembershipConfig(@RequestBody CreateMembershipConfigCommand command) {

        MembershipConfigId configId = this.createMembershipConfigUseCase.execute(command);

        GenericResponse<String> response = new GenericResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setId(configId.value());
        response.setMessage("Membership config created successfully.");
        response.setData(configId.value());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipConfig(@PathVariable String id) {
        MembershipConfigId configId = new MembershipConfigId(id);
        this.deleteMembershipConfigUseCase.execute(configId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<MembershipConfigResponse>> getMembershipConfigDetails(@PathVariable String id) {
        MembershipConfigId configId = new MembershipConfigId(id);

        MembershipConfig config = this.getMembershipConfigUseCase.execute(configId);

        GenericResponse<MembershipConfigResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainToDtoResponse(config));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/platform/{id}")
    public ResponseEntity<GenericResponse<List<MembershipConfigResponse>>> getMembershipsDetailsByPlatform(@PathVariable String id) {
        
        PlatformId platformId = new PlatformId(id);

        List<MembershipConfig> list = this.getAllMembershipConfigByPlatformUseCase.execute(platformId);

        GenericResponse<List<MembershipConfigResponse>> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(this.mapper.fromDomainListToDtoResponseList(list));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    
    
}
