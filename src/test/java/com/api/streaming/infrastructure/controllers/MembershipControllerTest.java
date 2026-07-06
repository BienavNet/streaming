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

import com.api.streaming.application.commands.CreateMembershipCommand;
import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.application.useCases.Membership.CreateMembershipUseCase;
import com.api.streaming.application.useCases.Membership.DeleteMembershipByAccountUseCase;
import com.api.streaming.application.useCases.Membership.GetMembershipByAccountIdUseCase;
import com.api.streaming.application.useCases.Membership.UpdateMembershipUseCase;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.dtos.membership.UpdateMembershipRequest;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.MembershipConfigMapperImpl;
import com.api.streaming.infrastructure.mappers.MembershipMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MembershipController.class)
@Import({
        MembershipMapperImpl.class, 
        AccountMapperImpl.class,
        PlatformMapperImpl.class,
        PaymentMapperImpl.class,
        MembershipConfigMapperImpl.class
    })
public class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.context.TestConfiguration
    static class JacksonTestConfig {
        @org.springframework.context.annotation.Bean
        public ObjectMapper testObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            return mapper;
        }
    }

    @MockitoBean
    private CreateMembershipUseCase createMembershipUseCase;

    @MockitoBean
    private DeleteMembershipByAccountUseCase deleteMembershipByAccountUseCase;

    @MockitoBean
    private GetMembershipByAccountIdUseCase getMembershipByAccountIdUseCase;

    @MockitoBean
    private UpdateMembershipUseCase updateMembershipUseCase;

    @Test
    void shouldCreateMembershipAndReturnCreated() throws Exception {
        CreateMembershipCommand command = new CreateMembershipCommand(new AccountId("ACC-1231"), new MembershipConfigId("cfg-46452"), false, LocalDate.now(), LocalDate.now().plusMonths(1));
        MembershipId membershipId = new MembershipId("mem-123");

        when(this.createMembershipUseCase.execute(any(CreateMembershipCommand.class))).thenReturn(membershipId);

        mockMvc.perform(
            post("/api/membership/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteMembershipByAccountAndReturnNoContent() throws Exception {
        String accountIdStr = "acc-123";
        AccountId accountId = new AccountId(accountIdStr);

        mockMvc.perform(
            delete("/api/membership/account/{id}", accountIdStr)
        )
        .andExpect(status().isNoContent());

        verify(this.deleteMembershipByAccountUseCase, times(1)).execute(accountId);
    }

    @Test
    void shouldGetMembershipDetailsByAccountAndReturnOk() throws Exception {
        String accountIdStr = "acc-123";
        MembershipDetailsQuery mockQuery = new MembershipDetailsQuery(
            new MembershipId("mem-123"),
            null, // Reemplazar con una instancia de MembershipConfig si es necesario
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
        );

        when(this.getMembershipByAccountIdUseCase.execute(any(AccountId.class))).thenReturn(mockQuery);

        mockMvc.perform(
            get("/api/membership/account/{id}", accountIdStr)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateMembershipAndReturnOk() throws Exception {
        String membershipIdStr = "mem-123";
        UpdateMembershipRequest request = new UpdateMembershipRequest(false, LocalDate.now(), LocalDate.now().plusMonths(2));

        mockMvc.perform(
            put("/api/membership/{id}", membershipIdStr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk());

        verify(this.updateMembershipUseCase, times(1)).execute(any(UpdateMembershipCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingMembership() throws Exception {
        CreateMembershipCommand command = new CreateMembershipCommand(new AccountId("ACC-1231"), new MembershipConfigId("cfg-46452"), false, LocalDate.now(), LocalDate.now().plusMonths(1));

        when(this.createMembershipUseCase.execute(any(CreateMembershipCommand.class)))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(
            post("/api/membership/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenDeletingMembershipWithNonExistentAccount() throws Exception {
        String accountIdStr = "non-existent-account";
        AccountId accountId = new AccountId(accountIdStr);

        doThrow(new EntityNotFoundException("Account not found"))
            .when(this.deleteMembershipByAccountUseCase)
            .execute(accountId);

        mockMvc.perform(
            delete("/api/membership/account/{id}", accountIdStr)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenMembershipDetailsAreNotFound() throws Exception {
        String accountIdStr = "non-existent-account";

        when(this.getMembershipByAccountIdUseCase.execute(any(AccountId.class)))
            .thenThrow(new EntityNotFoundException());

        mockMvc.perform(
            get("/api/membership/account/{id}", accountIdStr)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    }
}
