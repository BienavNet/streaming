package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.infrastructure.dtos.membershipConfig.MembershipConfigResponse;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PlatformMapper.class}
)

public interface MembershipConfigMapper {

    MembershipConfigMapper INSTANCE = Mappers.getMapper(MembershipConfigMapper.class);

    MembershipConfig fromEntityToDomain(MembershipConfigEntity entity);
    MembershipConfigEntity fromDomainToEntity(MembershipConfig domain);

    List<MembershipConfig> fromEntityListToDomainList(List<MembershipConfigEntity> entities);

    MembershipConfigResponse fromDomainToDtoResponse(MembershipConfig domain);
    List<MembershipConfigResponse> fromDomainListToDtoResponseList(List<MembershipConfig> domain);


    default MembershipConfigId mapFromStringToMembershipConfigId(String id) {
        return id != null ? new MembershipConfigId(id) : null;
    }

    default String mapFromMembershipConfigIdToString(MembershipConfigId id) {
        return id != null ? id.value() : null;
    }

}
