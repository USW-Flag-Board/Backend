package com.Flaground.backend.module.member.controller.mapper;

import com.Flaground.backend.module.member.controller.dto.request.UpdateAvatarRequest;
import com.Flaground.backend.module.member.controller.dto.response.RecoveryResponse;
import com.Flaground.backend.module.member.controller.dto.response.RecoveryResultResponse;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.token.domain.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapper {
    Avatar mapFrom(UpdateAvatarRequest updateAvatarRequest);
}
