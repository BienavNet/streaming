package com.api.streaming.application.useCases.Membership;


import static org.assertj.core.api.Assertions.assertThat;
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
        
        // Creamos un registro dummy usando el record
        this.sampleQuery = new MembershipDetailsQuery(
            membershipId,
            this.membershipConfigMock,
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
        );
    }

    @Test
    void shouldReturnListOfMembershipsSuccessfully() {
        // Given
        List<MembershipDetailsQuery> expectedList = List.of(this.sampleQuery);
        when(this.membershipDetailsInterface.getAllMembershipDetails())
            .thenReturn(expectedList);

        // When
        List<MembershipDetailsQuery> resultList = this.useCase.execute();

        // Then
        assertThat(resultList)
            .isNotEmpty()
            .hasSize(1)
            .containsExactly(this.sampleQuery);

        verify(this.membershipDetailsInterface, times(1)).getAllMembershipDetails();
    }

    @Test
    void shouldReturnEmptyListWhenNoMembershipsExist() {
        // Given
        when(this.membershipDetailsInterface.getAllMembershipDetails())
            .thenReturn(Collections.emptyList());

        // When
        List<MembershipDetailsQuery> resultList = this.useCase.execute();

        // Then
        assertThat(resultList)
            .isEmpty();

        verify(this.membershipDetailsInterface, times(1)).getAllMembershipDetails();
    }
}
