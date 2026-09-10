package com.api.streaming.infrastructure.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.queries.CronTaskQuery;
import com.api.streaming.infrastructure.persistence.entities.CronTask;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CronTaskMapper {
    CronTaskMapper INSTANCE = Mappers.getMapper(CronTaskMapper.class);

    CronTaskQuery fromEntityToQuery(CronTask entities);

    CronTask fromQueryToEntity(CronTaskQuery query);
}
