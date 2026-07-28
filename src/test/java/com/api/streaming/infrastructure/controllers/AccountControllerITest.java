package com.api.streaming.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.application.useCases.Account.CreateAccountUseCase;
import com.api.streaming.application.useCases.Account.DeactivateAccountUseCase;
import com.api.streaming.application.useCases.Account.DeleteAccountUseCase;
import com.api.streaming.application.useCases.Account.GetAccountFromIdUseCase;
import com.api.streaming.application.useCases.Account.GetAllAccountsUseCase;
import com.api.streaming.application.useCases.Account.UpdateAccountUseCase;
import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;

import jakarta.persistence.EntityNotFoundException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AccountController.class)
@Import({AccountMapperImpl.class, PaymentMapperImpl.class, PlatformMapperImpl.class})
public class AccountControllerITest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;
    @MockitoBean
    private DeactivateAccountUseCase deactivateAccountUseCase;
    @MockitoBean
    private DeleteAccountUseCase deleteAccountUseCase;
    @MockitoBean
    private GetAccountFromIdUseCase getAccountFromIdUseCase;
    @MockitoBean
    private GetAllAccountsUseCase getAllAccountsUseCase;
    @MockitoBean
    private UpdateAccountUseCase updateAccountUseCase;


    @Test
    void shouldCreateAccountAndReturnOK() throws Exception {
        AccountId accountId = new AccountId("uudi-982893");
        MembershipDetailsQuery membershipDetailsQuery = new MembershipDetailsQuery(new MembershipId("uuid-234982"), new MembershipConfig(new MembershipConfigId("uuid-289218"), new PlatformId("uuid-28930"), "plan 1", 5), false, LocalDate.MIN, LocalDate.MAX);
        AccountDetailsQuery createdAccount = new AccountDetailsQuery(new AccountId("uuid-32422"), new Email("test@gmail.com"), "23424234", LocalDate.now(), null, null, AccountStatus.ACTIVE, membershipDetailsQuery);

        when(this.createAccountUseCase.execute(any(CreateAccountCommand.class))).thenReturn(accountId);
        when(this.getAccountFromIdUseCase.execute(accountId)).thenReturn(createdAccount);

        String jsonManual = """
        {
            "email": "test@gmail.com",
            "password": "uuid-password123",
            "isFreeTrial": false,
            "membershipBuyDate": "2026-01-01",
            "membershipExpireDate": "2026-12-31",
            "platform": "uuid-4234",
            "payment": "uuid-87546"
        }
        """;

        this.mockMvc.perform(
            post("/api/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonManual)
        ).andDo(print())
        .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteAccountAndReturnOK() throws Exception {
        AccountId accountId = new AccountId("uuid0023");
        
        this.mockMvc.perform(
            delete("/api/account/{id}", accountId.value())
        ).andExpect(status().isNoContent());

        verify(this.deleteAccountUseCase, times(1)).execute(accountId);
    }

    @Test
    void shouldGetAccountDetailsAndReturnOK() throws Exception {
        AccountId accountId = new AccountId("uuid-23422");
        MembershipDetailsQuery membershipDetailsQuery = new MembershipDetailsQuery(new MembershipId("uuid-234982"), new MembershipConfig(new MembershipConfigId("uuid-289218"), new PlatformId("uuid-28930"), "plan 1", 5), false, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        AccountDetailsQuery mockAccount = new AccountDetailsQuery(accountId, new Email("test@gmail.com"), "23424234", LocalDate.now(), null, null, AccountStatus.ACTIVE, membershipDetailsQuery);

        when(this.getAccountFromIdUseCase.execute(accountId)).thenReturn(mockAccount);

        this.mockMvc.perform(
            get("/api/account/{id}", accountId.value())
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldGetAllAccountDetailsAndReturnOK() throws Exception {
        MembershipDetailsQuery membershipDetailsQuery = new MembershipDetailsQuery(new MembershipId("uuid-234982"), new MembershipConfig(new MembershipConfigId("uuid-289218"), new PlatformId("uuid-28930"), "plan 1", 5), false, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        AccountDetailsQuery mock = new AccountDetailsQuery(new AccountId("uuid-32422"), new Email("test@gmail.com"), "23424234", LocalDate.now(), null, null, AccountStatus.ACTIVE, membershipDetailsQuery);
        List<AccountDetailsQuery> mockList = List.of(mock, mock);

        when(this.getAllAccountsUseCase.execute()).thenReturn(mockList);

        this.mockMvc.perform(
            get("/api/account/details")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldUpdateAccountAndReturnOK() throws Exception {
        AccountId accountId = new AccountId("uuid-22242");

        String jsonManual = """
        {
            "email": "test@gmail.com",
            "password": "uuid-password123",
            "isFreeTrial": false,
            "platform": "uuid-4234",
            "payment": "uuid-87546"
        }
        """;

        when(this.updateAccountUseCase.execute(any(UpdateAccountCommand.class))).thenReturn(accountId);

        this.mockMvc.perform(
            put("/api/account/{id}", accountId.value())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonManual)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingAccount() throws Exception {
        when(this.createAccountUseCase.execute(any(CreateAccountCommand.class))).thenThrow(new IllegalArgumentException());

        String jsonManual = """
        {
            "email": "test@gmail.com",
            "password": "uuid-password123",
            "isFreeTrial": false,
            "membershipBuyDate": "2026-01-01",
            "membershipExpireDate": "2026-12-31",
            "platform": "uuid-4234",
            "payment": "uuid-87546"
        }
        """;

        this.mockMvc.perform(
            post("/api/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonManual)
        ).andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingAccount() throws Exception {
        AccountId accountId = new AccountId("uuid0023");

        doThrow(new EntityNotFoundException()).when(this.deleteAccountUseCase).execute(accountId);
        
        this.mockMvc.perform(
            delete("/api/account/{id}", accountId.value())
        ).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionGettingDetailsWhenAccountDoesNotExist() throws Exception {
        AccountId accountId = new AccountId("uuid-23422");
        

        when(this.getAccountFromIdUseCase.execute(accountId)).thenThrow(new EntityNotFoundException());

        this.mockMvc.perform(
            get("/api/account/{id}", accountId.value())
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAccount() throws Exception {
        AccountId accountId = new AccountId("uuid-22242");

        String jsonManual = """
        {
            "email": "test@gmail.com",
            "password": "uuid-password123",
            "isFreeTrial": false,
            "platform": "uuid-4234",
            "payment": "uuid-87546"
        }
        """;

        when(this.updateAccountUseCase.execute(any(UpdateAccountCommand.class))).thenThrow(new EntityNotFoundException());

        this.mockMvc.perform(
            put("/api/account/{id}", accountId.value())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonManual)
        ).andDo(print()).andExpect(status().isNotFound());
    }
}
