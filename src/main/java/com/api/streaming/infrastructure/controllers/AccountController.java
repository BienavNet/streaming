package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.application.useCases.Account.CreateAccountUseCase;
import com.api.streaming.application.useCases.Account.DeactivateAccountUseCase;
import com.api.streaming.application.useCases.Account.DeleteAccountUseCase;
import com.api.streaming.application.useCases.Account.GetAccountFromIdUseCase;
import com.api.streaming.application.useCases.Account.GetAllAccountsUseCase;
import com.api.streaming.application.useCases.Account.UpdateAccountUseCase;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.account.AccountDetailsResponse;
import com.api.streaming.infrastructure.dtos.account.AccountResponse;
import com.api.streaming.infrastructure.dtos.account.CreateAccountRequest;
import com.api.streaming.infrastructure.dtos.account.UpdateAccountRequest;
import com.api.streaming.infrastructure.mappers.AccountMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

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
    private final DeactivateAccountUseCase deactivateAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GetAccountFromIdUseCase getAccountFromIdUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final AccountMapper mapper;

    @PostMapping("")
    public ResponseEntity<GenericResponse> saveAccount(@RequestBody CreateAccountRequest request) {
        CreateAccountCommand command = this.mapper.toCreateAccountCommandFromDto(request);
        String id = this.createAccountUseCase.execute(command).value();
        GenericResponse response = GenericResponse.builder().id(id).status(HttpStatus.OK.value()).build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("deactivate/{id}")
    public ResponseEntity<GenericResponse> deactivateAccount(@PathVariable String id) {

        AccountId accountId = new AccountId(id);

        this.deactivateAccountUseCase.execute(accountId);

        GenericResponse response = GenericResponse.builder().id(id).status(HttpStatus.OK.value()).message("Account deactivate successfully").build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteAccount(@PathVariable String id) {
        AccountId accountId = new AccountId(id);

        this.deleteAccountUseCase.execute(accountId);
        GenericResponse response = GenericResponse.builder().id(id).status(HttpStatus.OK.value()).build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/details")
    public ResponseEntity<List<AccountDetailsResponse>> getAccountDetails() {
        List<AccountDetailsQuery> accounts = this.getAllAccountsUseCase.execute();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.mapper.fromQueryToResponse(accounts));

    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getPaymentById(@PathVariable String id) {
        AccountId accountId = new AccountId(id);

        AccountDetailsQuery account = this.getAccountFromIdUseCase.execute(accountId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.mapper.fromQueryToAccountResponse(account));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<GenericResponse> updateAccount(@PathVariable String id, @RequestBody UpdateAccountRequest request) {
        AccountId accountId = new AccountId(id);

        UpdateAccountCommand command = this.mapper.fromUpdateRequestToUpdateCommand(accountId, request);

        this.updateAccountUseCase.execute(command);

        GenericResponse response = GenericResponse.builder().id(accountId.value()).message("Entity updated successfully").build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);


    }
    
    
    
}
