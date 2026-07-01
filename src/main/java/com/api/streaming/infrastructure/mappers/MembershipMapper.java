package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AccountMapper.class, MembershipConfigMapper.class}
)
public interface MembershipMapper {
    
    Membership fromEntityToDomain(MembershipEntity entity);
    MembershipEntity fromDomainToEntity(Membership domain);

    List<Membership> fromEntityListToDomainList(List<MembershipEntity> entityList);

    @Mapping(
        target = "details",
        source = "details"
    )
    MembershipDetailsQuery fromEntityToQuery(String id, MembershipConfigEntity details);


    default MembershipId mapFromStringToMembershipId(String id) {
        return id!=null ? new MembershipId(id) : null;
    }

    default String mapFromMembershipIdToString(MembershipId id) {
        return id!=null ? id.value() : null;
    }
}
