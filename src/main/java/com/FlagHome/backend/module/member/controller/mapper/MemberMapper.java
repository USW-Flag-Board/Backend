package com.FlagHome.backend.module.member.controller.mapper;

import com.FlagHome.backend.module.member.controller.dto.request.UpdateAvatarRequest;
import com.FlagHome.backend.module.member.controller.dto.response.RecoveryResponse;
import com.FlagHome.backend.module.member.controller.dto.response.RecoveryResultResponse;
import com.FlagHome.backend.module.member.domain.Avatar;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.token.domain.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapper {
    Avatar mapFrom(UpdateAvatarRequest updateAvatarRequest);

    @Mapping(source = "key", target = "email")
    @Mapping(source = "expiredAt", target = "deadLine")
    RecoveryResponse toRecoveryResponse(Token findRequestToken);

    RecoveryResultResponse toRecoveryResult(Member member);
}
