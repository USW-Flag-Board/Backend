package com.FlagHome.backend.module.member.mapper;

import com.FlagHome.backend.module.member.controller.dto.request.UpdateAvatarRequest;
import com.FlagHome.backend.module.member.domain.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapper {
    Avatar mapFrom(UpdateAvatarRequest updateAvatarRequest);
}
