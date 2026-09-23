package com.api.streaming.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.domain.models.User;
import com.api.streaming.domain.valueObjects.UserId;
import com.api.streaming.infrastructure.persistence.entities.UserEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User fromEntityToDomain(UserEntity entity);
    UserEntity fromDomainToEntity(User user);

    default UserId mapStringToUserId(String id) {
        return  new UserId(id);
    }

    default String mapUserIdToString(UserId id) {
        return  id.value();
    }
}
