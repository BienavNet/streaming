package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.commands.CreateMembershipCommand;
import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.dtos.membership.CreateMembershipRequest;
import com.api.streaming.infrastructure.dtos.membership.MembershipDetailsResponse;
import com.api.streaming.infrastructure.dtos.membership.UpdateMembershipRequest;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AccountMapper.class, MembershipConfigMapper.class}
)
public interface MembershipMapper {

    MembershipMapper INSTANCE = Mappers.getMapper(MembershipMapper.class);
    
    Membership fromEntityToDomain(MembershipEntity entity);


    MembershipEntity fromDomainToEntity(Membership domain);

    void updateMembershipFromCommand(UpdateMembershipCommand command, @MappingTarget Membership target);

    List<Membership> fromEntityListToDomainList(List<MembershipEntity> entityList);

    @Mapping(
        target = "id",
        source = "entity.id"
    )
    MembershipDetailsQuery fromEntityToQuery(MembershipEntity entity, MembershipConfigEntity details);

    @Mapping(
        target = "membershipId",
        source = "id"
    )
    UpdateMembershipCommand fromUpdateRequestToUpdateCommand(MembershipId id, UpdateMembershipRequest request);

    
    CreateMembershipCommand fromCreateRequestToCreateCommand(CreateMembershipRequest request);

    MembershipDetailsResponse fromMembershipQueryToMembershipResponse(MembershipDetailsQuery query);

    default MembershipId mapFromStringToMembershipId(String id) {
        return id!=null ? new MembershipId(id) : null;
    }

    default String mapFromMembershipIdToString(MembershipId id) {
        return id!=null ? id.value() : null;
    }
}
