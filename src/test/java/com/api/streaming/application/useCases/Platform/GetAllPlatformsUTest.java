package com.api.streaming.application.useCases.Platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;

@ExtendWith(MockitoExtension.class)
public class GetAllPlatformsUTest {
    
    @Mock
    private PlatformInterface platformInterface;

    @InjectMocks
    private GetAllPlatformsUseCase getAllPlatformsUseCase;

    @Test
    void shouldReturnListOfPlatformsSuccessfully() {
        Platform platform1 = new Platform(new PlatformId("uuid-2342"), "Netflix", "https://www.netflix.com", "netflix-icon");
        
        Platform platform2 = new Platform(new PlatformId("uuid-5234"), "HBO", "https://www.hbo.com", "hbo-icon");

        List<Platform> mockPlatformList = List.of(platform1, platform2);

        when(this.platformInterface.getAllPlatforms()).thenReturn(mockPlatformList);

        assertThat(this.getAllPlatformsUseCase.execute())
        .isNotNull()
        .containsExactly(platform1, platform2);

        verify(this.platformInterface, times(1)).getAllPlatforms();
    }

    @Test
    void shouldReturnEmptyListOfPlatforms() {

        when(this.platformInterface.getAllPlatforms()).thenReturn(List.of());

        assertThat(this.getAllPlatformsUseCase.execute())
        .hasSize(0)
        .isEmpty();

        verify(this.platformInterface, times(1)).getAllPlatforms();
    }
}
