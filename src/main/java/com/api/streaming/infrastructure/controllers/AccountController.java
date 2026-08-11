package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.application.useCases.Account.CreateAccountUseCase;
import com.api.streaming.application.useCases.Account.DeleteAccountUseCase;
import com.api.streaming.application.useCases.Account.GetAccountFromIdUseCase;
import com.api.streaming.application.useCases.Account.GetAllAccountsUseCase;
import com.api.streaming.application.useCases.Account.UpdateAccountUseCase;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.account.AccountDetailsResponse;
import com.api.streaming.infrastructure.dtos.account.AccountResponse;
import com.api.streaming.infrastructure.dtos.account.CreateAccountRequest;
import com.api.streaming.infrastructure.dtos.account.UpdateAccountRequest;
import com.api.streaming.infrastructure.mappers.AccountMapper;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;



 
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GetAccountFromIdUseCase getAccountFromIdUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final AccountMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse<AccountResponse>> saveAccount(@RequestBody CreateAccountRequest request) {
        CreateAccountCommand command = this.mapper.toCreateAccountCommandFromDto(request);

        AccountId createdAccountId = this.createAccountUseCase.execute(command);
        AccountDetailsQuery createdAccount = this.getAccountFromIdUseCase.execute(createdAccountId);

        GenericResponse<AccountResponse> response = new GenericResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setId(createdAccountId.value());
        response.setMessage("Account created successfully.");
        response.setData(this.mapper.fromQueryToAccountResponse(createdAccount));

        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        AccountId accountId = new AccountId(id);
        this.deleteAccountUseCase.execute(accountId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/details/{page}/{offset}")
    public ResponseEntity<GenericResponse<Page<AccountDetailsResponse>>> getAccountDetails(@PathVariable int page, @PathVariable int offset) throws Exception {

        Pageable pageable = new Pageable(page, offset);

        Page<AccountDetailsResponse> accounts = this.getAllAccountsUseCase.execute(pageable).map(mapper::toResponse);

        GenericResponse<Page<AccountDetailsResponse>> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(accounts);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<AccountResponse>> getPaymentById(@PathVariable String id) {
        AccountId accountId = new AccountId(id);

        AccountDetailsQuery account = this.getAccountFromIdUseCase.execute(accountId);

        GenericResponse<AccountResponse> response = new GenericResponse<>();
        response.setData(this.mapper.fromQueryToAccountResponse(account));
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<AccountResponse>> updateAccount(@PathVariable String id, @RequestBody UpdateAccountRequest request) {
        AccountId accountId = new AccountId(id);

        UpdateAccountCommand command = this.mapper.fromUpdateRequestToUpdateCommand(accountId, request);

        this.updateAccountUseCase.execute(command);

        AccountDetailsQuery accountUpdated = this.getAccountFromIdUseCase.execute(accountId);

        GenericResponse<AccountResponse> response = new GenericResponse<>();
        response.setId(accountId.value());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Entity updated successfully");
        response.setData(this.mapper.fromQueryToAccountResponse(accountUpdated));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
