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

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.streaming.application.commands.CreatePlatformCommand;
import com.api.streaming.application.commands.UpdatePlatformCommand;
import com.api.streaming.application.useCases.Platform.CreatePlatformUseCase;
import com.api.streaming.application.useCases.Platform.DeletePlatformUseCase;
import com.api.streaming.application.useCases.Platform.GetAllPlatformsUseCase;
import com.api.streaming.application.useCases.Platform.GetPlatformUseCase;
import com.api.streaming.application.useCases.Platform.UpdatePlatformUseCase;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.dtos.payment.CreatePaymentRequest;
import com.api.streaming.infrastructure.dtos.platform.CreatePlatformRequest;
import com.api.streaming.infrastructure.dtos.platform.UpdatePlatformRequest;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;

import jakarta.persistence.EntityNotFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PlatformController.class)
@Import({PlatformMapperImpl.class})
public class PlatformControllerITest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreatePlatformUseCase createPlatformUseCase;
    @MockitoBean
    private DeletePlatformUseCase deletePlatformUseCase;
    @MockitoBean
    private GetAllPlatformsUseCase getAllPlatformsUseCase;
    @MockitoBean
    private GetPlatformUseCase getPlatformUseCase;
    @MockitoBean
    private UpdatePlatformUseCase updatePlatformUseCase;

    @Test
    void shouldCreatePlatformAndReturnOK() throws Exception {
        CreatePlatformRequest request = new CreatePlatformRequest("netflix", "https://www.netflix.com", "icon");

        PlatformId platformId = new PlatformId("uuid-23422");

        when(this.createPlatformUseCase.execute(any(CreatePlatformCommand.class))).thenReturn(platformId);

        this.mockMvc.perform(
            post("/api/platform")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldDeletePlatformAndReturnOK() throws Exception {
        PlatformId platformId = new PlatformId("uuid-2342");

        this.mockMvc.perform(
            delete("/api/platform/{id}", platformId.value())
        )
        .andExpect(status().isNoContent());

        verify(this.deletePlatformUseCase, times(1)).execute(platformId);
    }

    @Test
    void shouldGetAllPlatforms() throws Exception {
        Platform platformMock = new Platform(new PlatformId("uuid-2342"), "netflix", "www.netflix.com", "icon");

        List<Platform> listMock = List.of(platformMock, platformMock);

        when(this.getAllPlatformsUseCase.execute()).thenReturn(listMock);

        this.mockMvc.perform(
            get("/api/platform")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldGetPlatformById() throws Exception {
        PlatformId platformId = new PlatformId("uuid-234298");
        Platform platformMock = new Platform( platformId, "netflix", "www.netflix.com", "icon");

        when(this.getPlatformUseCase.execute(platformId)).thenReturn(platformMock);

        this.mockMvc.perform(
            get("/api/platform/{id}", platformId.value())
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateAnExistingPlatformAndReturnOK() throws JacksonException, Exception {
        PlatformId platformId = new PlatformId("uuid-234298");
        Platform platformMockUpdated = new Platform( platformId, "netflix", "www.netflix.com", "icon");

        UpdatePlatformCommand command = new UpdatePlatformCommand(platformId, "null", "null", "null");

        when(this.updatePlatformUseCase.execute(command)).thenReturn(platformMockUpdated);

        this.mockMvc.perform(
            put("/api/platform/{id}", platformId.value())
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(command))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingPlatform() throws Exception {
        CreatePaymentRequest request = new CreatePaymentRequest();

        when(this.createPlatformUseCase.execute(any(CreatePlatformCommand.class))).thenThrow(new IllegalArgumentException());

        this.mockMvc.perform(
            post("/api/platform")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenDeletingPlatformThatDoesNotExisting() throws Exception {
        PlatformId platformId = new PlatformId("uuid-23422");

        doThrow(new EntityNotFoundException())
        .when(this.deletePlatformUseCase)
        .execute(platformId);

        this.mockMvc.perform(
            delete("/api/platform/{id}", platformId.value())
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenConsultingPlatformThatDoesNotExist() throws Exception{
        when(this.getPlatformUseCase.execute(any(PlatformId.class))).thenThrow(new EntityNotFoundException());

        this.mockMvc.perform(
            get("/api/platform/{id}", "uuid-23424")
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPlatformThatDoesNotExist() throws JacksonException, Exception {
        UpdatePlatformRequest request = new UpdatePlatformRequest();

        when(this.updatePlatformUseCase.execute(any(UpdatePlatformCommand.class))).thenThrow(new EntityNotFoundException());

        this.mockMvc.perform(
            put("/api/platform/{id}", "uuid-2342")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound());
    }

}
