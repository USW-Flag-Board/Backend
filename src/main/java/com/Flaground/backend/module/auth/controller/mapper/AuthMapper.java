package com.Flaground.backend.module.auth.controller.mapper;

import com.Flaground.backend.global.utility.RandomGenerator;
import com.Flaground.backend.module.auth.controller.dto.request.JoinRequest;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface AuthMapper {
    @Mapping(target = "certification", expression = "java(getRandomNumber())")
    AuthInformation mapFrom(JoinRequest joinRequest);

    default String getRandomNumber() {
        return RandomGenerator.getRandomNumber();
    }
}
