package com.Flaground.backend.module.auth.controller.mapper;

import com.Flaground.backend.module.auth.controller.dto.request.JoinRequest;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface AuthMapper {
    @Mapping(target = "certification", expression = "java(com.FlagHome.backend.global.utility.RandomGenerator.getRandomNumber())")
    AuthInformation mapFrom(JoinRequest joinRequest);
}
