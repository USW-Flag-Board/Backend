package com.FlagHome.backend.domain.auth.mapper;

import com.FlagHome.backend.domain.auth.controller.dto.request.JoinRequest;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface AuthMapper {
    @Mapping(target = "certification", ignore = true)
    AuthInformation toEntity(JoinRequest joinRequest);
}
