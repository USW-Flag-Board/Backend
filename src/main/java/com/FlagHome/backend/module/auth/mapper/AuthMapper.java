package com.FlagHome.backend.module.auth.mapper;

import com.FlagHome.backend.global.utility.RandomGenerator;
import com.FlagHome.backend.module.auth.mapper.vo.JoinData;
import com.FlagHome.backend.module.auth.domain.AuthInformation;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface AuthMapper {
    @Mapping(target = "certification", qualifiedByName = "getCertification")
    AuthInformation mapFrom(JoinData joinData);

    @Named("getCertification")
    static String getCertification() {
        return RandomGenerator.getRandomNumber();
    }
}
