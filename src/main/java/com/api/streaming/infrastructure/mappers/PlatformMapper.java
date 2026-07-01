package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.commands.CreatePlatformCommand;
import com.api.streaming.application.commands.UpdatePlatformCommand;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.dtos.platform.CreatePlatformRequest;
import com.api.streaming.infrastructure.dtos.platform.PlatformResponse;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PlatformMapper {

    PlatformMapper INSTANCE = Mappers.getMapper(PlatformMapper.class);

    
    Platform fromEntityToDomain(PlatformEntity entity);
    PlatformEntity fromDomainToEntity(Platform domain);
    
    List<Platform> fromEntityListToDomainList(List<PlatformEntity> entities);
    
    CreatePlatformCommand createPlatformFromDtoToCommand(CreatePlatformRequest request);
    
    PlatformResponse fromDomainToDto(Platform domain);
    List<PlatformResponse> fromDomainListToDtoList(List<Platform> platforms);
    
    // Mapping for updating from Command to Domain
    @Mapping(source = "id", target = "id", ignore = true)
    void UpdatePlatformFromCommand(UpdatePlatformCommand source, @MappingTarget Platform target);


    default String mapPlatformIdToString(PlatformId platform) {
        return platform != null ? platform.value() : null;
    }

    default PlatformId mapStringToPlatformId(String platform) {
        return platform == null ? null : new PlatformId(platform);
    }
}
