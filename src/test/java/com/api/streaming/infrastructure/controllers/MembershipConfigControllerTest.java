package com.api.streaming.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.streaming.application.commands.CreateMembershipConfigCommand;
import com.api.streaming.application.useCases.MembershipConfig.CreateMembershipConfigUseCase;
import com.api.streaming.application.useCases.MembershipConfig.DeleteMembershipConfigUseCase;
import com.api.streaming.application.useCases.MembershipConfig.GetAllMembershipConfigByPlatformUseCase;
import com.api.streaming.application.useCases.MembershipConfig.GetMembershipConfigUseCase;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.MembershipConfigMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MembershipConfigController.class)
@Import(
    {
        MembershipConfigMapperImpl.class, 
        PlatformMapperImpl.class,
    }
)
public class MembershipConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.context.TestConfiguration
    static class JacksonTestConfig {
        @org.springframework.context.annotation.Bean
        public ObjectMapper testObjectMapper() {
            // Esto provee un ObjectMapper básico listo para usar en tus pruebas
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            return mapper;
        }
    }

    @MockitoBean
    private CreateMembershipConfigUseCase createMembershipConfigUseCase;

    @MockitoBean
    private DeleteMembershipConfigUseCase deleteMembershipConfigUseCase;

    @MockitoBean
    private GetAllMembershipConfigByPlatformUseCase getAllMembershipConfigByPlatformUseCase;

    @MockitoBean
    private GetMembershipConfigUseCase getMembershipConfigUseCase;

    @Test
    void shouldCreateMembershipConfigAndReturnCreated() throws Exception {

        CreateMembershipConfigCommand command = new CreateMembershipConfigCommand(new PlatformId("plat-8787"), "basic", 10);

        MembershipConfigId configId = new MembershipConfigId("config-123");

        when(this.createMembershipConfigUseCase.execute(any(CreateMembershipConfigCommand.class))).thenReturn(configId);

        mockMvc.perform(
            post("/api/membership/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteMembershipConfigAndReturnNoContent() throws Exception {
        String idToDelete = "config-123";
        MembershipConfigId configIdToDelete = new MembershipConfigId(idToDelete);

        mockMvc.perform(
            delete("/api/membership/config/{id}", idToDelete)
        )
        .andExpect(status().isNoContent());

        verify(this.deleteMembershipConfigUseCase, times(1)).execute(configIdToDelete);
    }

    @Test
    void shouldGetMembershipConfigDetailsAndReturnOk() throws Exception {
        String idToFind = "config-123";
        MembershipConfig mockConfig = new MembershipConfig(new MembershipConfigId(idToFind), new PlatformId("NETFLIX"), "PREMIUM", 4);

        when(this.getMembershipConfigUseCase.execute(any(MembershipConfigId.class))).thenReturn(mockConfig);

        mockMvc.perform(
            get("/api/membership/config/{id}", idToFind)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldGetMembershipsDetailsByPlatformAndReturnOk() throws Exception {
        String platformIdStr = "NETFLIX";
        MembershipConfig mockConfig1 = new MembershipConfig(new MembershipConfigId("c1"), new PlatformId(platformIdStr), "BASIC", 1);
        MembershipConfig mockConfig2 = new MembershipConfig(new MembershipConfigId("c2"), new PlatformId(platformIdStr), "PREMIUM", 4);

        when(this.getAllMembershipConfigByPlatformUseCase.execute(any(PlatformId.class)))
            .thenReturn(List.of(mockConfig1, mockConfig2));

        mockMvc.perform(
            get("/api/membership/config/platform/{id}", platformIdStr)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingMembershipConfig() throws Exception {
        CreateMembershipConfigCommand command = new CreateMembershipConfigCommand(null, "PREMIUM", 4);

        when(this.createMembershipConfigUseCase.execute(any(CreateMembershipConfigCommand.class)))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(
            post("/api/membership/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenDeletingMembershipConfigThatDoesNotExist() throws Exception {
        String idToDelete = "non-existent";
        MembershipConfigId configIdToDelete = new MembershipConfigId(idToDelete);

        doThrow(new EntityNotFoundException("Config not found"))
            .when(this.deleteMembershipConfigUseCase)
            .execute(configIdToDelete);

        mockMvc.perform(
            delete("/api/membership/config/{id}", idToDelete)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenMembershipConfigIsNotFound() throws Exception {
        String idToFind = "non-existent";

        when(this.getMembershipConfigUseCase.execute(any(MembershipConfigId.class)))
            .thenThrow(new EntityNotFoundException());

        mockMvc.perform(
            get("/api/membership/config/{id}", idToFind)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    }
}

