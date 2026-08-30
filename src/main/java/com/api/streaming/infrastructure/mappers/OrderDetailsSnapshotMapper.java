package com.api.streaming.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;
import com.api.streaming.infrastructure.persistence.entities.OrderDetailsSnapshot;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AccountMapper.class, MembershipConfigMapper.class}
)
public interface OrderDetailsSnapshotMapper {
    MembershipMapper INSTANCE = Mappers.getMapper(MembershipMapper.class);

    OrderDetailsSnapshotQuery fromEntityToDomain(OrderDetailsSnapshot snapshot);
    OrderDetailsSnapshot fromDomainToEntity(OrderDetailsSnapshotQuery query);
}
