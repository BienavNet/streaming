package com.api.streaming.application.useCases.Membership;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;


@ExtendWith(MockitoExtension.class)
public class GetAllMembershipUTest {

    @Mock
    private MembershipDetailsInterface membershipDetailsInterface;

    @Mock
    private MembershipConfig membershipConfigMock;

    @InjectMocks
    private GetAllMembershipsUseCase useCase;

    private MembershipDetailsQuery sampleQuery;

    @BeforeEach
    void setup() {
        MembershipId membershipId = new MembershipId("uuid-memb-123");
        
        this.sampleQuery = new MembershipDetailsQuery(
            membershipId,
            this.membershipConfigMock,
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
        );
    }

    @Test
    void shouldReturnPageOfMembershipsSuccessfully() {
        // Given
        Pageable pageable = new Pageable(0, 10);
        Page<MembershipDetailsQuery> expectedPage = new Page<>(List.of(this.sampleQuery), 1L, 1);
        
        when(this.membershipDetailsInterface.getAllMembershipDetails(pageable))
            .thenReturn(expectedPage);

        // When
        Page<MembershipDetailsQuery> resultPage = this.useCase.execute(pageable);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent())
            .isNotEmpty()
            .hasSize(1)
            .containsExactly(this.sampleQuery);
        assertThat(resultPage.getTotalElements()).isEqualTo(1L);
        assertThat(resultPage.getTotalPages()).isEqualTo(1);

        verify(this.membershipDetailsInterface, times(1)).getAllMembershipDetails(pageable);
    }

    @Test
    void shouldAssignDefaultPageableWhenNullIsProvided() {
        // Given
        Pageable defaultPageable = new Pageable(0, 10);
        Page<MembershipDetailsQuery> expectedPage = new Page<>(List.of(this.sampleQuery), 1L, 1);
        
        when(this.membershipDetailsInterface.getAllMembershipDetails(any(Pageable.class)))
            .thenReturn(expectedPage);

        // When
        Page<MembershipDetailsQuery> resultPage = this.useCase.execute(null);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).hasSize(1);

        // Verificamos que se llamó con el Pageable por defecto (page=0, offset=10)
        verify(this.membershipDetailsInterface, times(1)).getAllMembershipDetails(defaultPageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoMembershipsExist() {
        // Given
        Pageable pageable = new Pageable(0, 10);
        Page<MembershipDetailsQuery> expectedPage = new Page<>(Collections.emptyList(), 0L, 0);
        
        when(this.membershipDetailsInterface.getAllMembershipDetails(pageable))
            .thenReturn(expectedPage);

        // When
        Page<MembershipDetailsQuery> resultPage = this.useCase.execute(pageable);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).isEmpty();
        assertThat(resultPage.getTotalElements()).isEqualTo(0L);

        verify(this.membershipDetailsInterface, times(1)).getAllMembershipDetails(pageable);
    }
}