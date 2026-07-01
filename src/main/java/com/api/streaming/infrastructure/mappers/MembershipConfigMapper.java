package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PlatformMapper.class}
)

public interface MembershipConfigMapper {


    MembershipConfig fromEntityToDomain(MembershipConfigEntity entity);
    MembershipConfigEntity fromDomainToEntity(MembershipConfig domain);

    List<MembershipConfig> fromEntityListToDomainList(List<MembershipConfigEntity> entities);


    default MembershipConfigId mapFromStringToMembershipConfigId(String id) {
        return id != null ? new MembershipConfigId(id) : null;
    }

    default String mapFromMembershipConfigIdToString(MembershipConfigId id) {
        return id != null ? id.value() : null;
    }

}
