package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreateMembershipCommand;
import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.application.useCases.Membership.CreateMembershipUseCase;
import com.api.streaming.application.useCases.Membership.DeleteMembershipByAccountUseCase;
import com.api.streaming.application.useCases.Membership.GetMembershipByAccountIdUseCase;
import com.api.streaming.application.useCases.Membership.UpdateMembershipUseCase;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.membership.UpdateMembershipRequest;
import com.api.streaming.infrastructure.mappers.MembershipMapper;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/membership/")
@RequiredArgsConstructor
public class MembershipController {
    private final CreateMembershipUseCase createMembershipUseCase;
    private final DeleteMembershipByAccountUseCase deleteMembershipByAccountUseCase;
    private final GetMembershipByAccountIdUseCase getMembershipByAccountIdUseCase;
    private final UpdateMembershipUseCase updateMembershipUseCase;

    private final MembershipMapper mapper;


    @PostMapping("")
    public ResponseEntity<GenericResponse<String>> saveMembership(@RequestBody CreateMembershipCommand command) {
        MembershipId id = this.createMembershipUseCase.execute(command);

        GenericResponse<String> response = new GenericResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setId(id.value());
        response.setMessage("Membership created succesfully.");
        response.setData(id.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteMembershipById(@PathVariable String id) {
        AccountId accountId = new AccountId(id);

        this.deleteMembershipByAccountUseCase.execute(accountId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<GenericResponse<MembershipDetailsQuery>> getMembershipDetails(@PathVariable String id) {
        AccountId accountId = new AccountId(id);

        MembershipDetailsQuery query = this.getMembershipByAccountIdUseCase.execute(accountId);

        GenericResponse<MembershipDetailsQuery> response = new GenericResponse<>();
        response.setId(id);
        response.setData(query);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> updateMembership(@PathVariable String id, @RequestBody UpdateMembershipRequest request) {

        MembershipId membershipId = new MembershipId(id);

        UpdateMembershipCommand command = this.mapper.fromUpdateRequestToUpdateCommand(membershipId, request);

        this.updateMembershipUseCase.execute(command);

        GenericResponse<String> response = new GenericResponse<>();
        response.setId(id);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Membership updated successfully.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
    
}
