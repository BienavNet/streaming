package com.api.streaming.application.useCases.MembershipConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

@ExtendWith(MockitoExtension.class)
public class GetAllConfigByPlatformUTest {
    @Mock
    private MembershipConfigInterface membershipConfigInterface;

    @InjectMocks
    private GetAllMembershipConfigByPlatformUseCase useCase;

    private PlatformId platformId;
    private MembershipConfig configBasic;
    private MembershipConfig configPremium;

    @BeforeEach
    void setup() {
        this.platformId = new PlatformId("uuid-3298482");
        
        // Creamos un par de configuraciones mockeadas usando las propiedades que indicaste
        this.configBasic = new MembershipConfig(
            new MembershipConfigId("uuid-092309"), 
            this.platformId, 
            "basic", 
            2
        );
        
        this.configPremium = new MembershipConfig(
            new MembershipConfigId("uuid-884721"), 
            this.platformId, 
            "premium", 
            4
        );
    }

    @Test
    void shouldReturnListOfMembershipConfigsForPlatform() {
        // Given
        List<MembershipConfig> expectedList = List.of(this.configBasic, this.configPremium);
        when(this.membershipConfigInterface.getMembershipConfigsByPlatform(this.platformId))
            .thenReturn(expectedList);

        // When
        List<MembershipConfig> result = this.useCase.execute(this.platformId);
        
        // Then
        assertThat(result)
            .isNotNull()
            .hasSize(2)
            .containsExactlyInAnyOrder(this.configBasic, this.configPremium);

        verify(this.membershipConfigInterface, times(1))
            .getMembershipConfigsByPlatform(this.platformId);
    }

    @Test
    void shouldReturnEmptyListWhenNoConfigsExistForPlatform() {
        // Given
        when(this.membershipConfigInterface.getMembershipConfigsByPlatform(this.platformId))
            .thenReturn(Collections.emptyList());

        // When
        List<MembershipConfig> result = this.useCase.execute(this.platformId);

        // Then
        assertThat(result)
            .isNotNull()
            .isEmpty();

        verify(this.membershipConfigInterface, times(1))
            .getMembershipConfigsByPlatform(this.platformId);
    }
}
