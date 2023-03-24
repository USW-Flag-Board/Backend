package com.FlagHome.backend.domain.member.mapper;

import com.FlagHome.backend.domain.member.avatar.dto.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapper {
    default Avatar toAvatar(UpdateAvatarRequest updateAvatarRequest) {
        return Avatar.builder()
                .nickName(updateAvatarRequest.getNickName())
                .bio(updateAvatarRequest.getBio())
                .build();
    }
}
