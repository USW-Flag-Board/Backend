package com.FlagHome.backend.domain.member.mapper;

import com.FlagHome.backend.domain.member.controller.dto.request.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapper {
    default Avatar toAvatar(UpdateAvatarRequest updateAvatarRequest) {
        return Avatar.builder()
                .nickname(updateAvatarRequest.getNickName())
                .studentId(updateAvatarRequest.getStudentId())
                .major(updateAvatarRequest.getMajor())
                .bio(updateAvatarRequest.getBio())
                .build();
    }
}
