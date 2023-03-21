package com.FlagHome.backend.domain.member.mapper;

import com.FlagHome.backend.domain.member.avatar.dto.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Avatar toAvatar(UpdateAvatarRequest updateAvatarRequest);
}
