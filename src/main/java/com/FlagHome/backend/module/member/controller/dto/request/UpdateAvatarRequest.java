package com.FlagHome.backend.module.member.controller.dto.request;

import com.FlagHome.backend.module.member.domain.enums.Major;
import com.FlagHome.backend.global.annotation.EnumFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAvatarRequest {
    @Schema(name = "닉네임")
    @NotBlank
    private String nickname;

    @Schema(name = "학번")
    @NotBlank
    private String studentId;

    @Schema(name = "전공")
    @EnumFormat(enumClass = Major.class)
    private Major major;

    @Schema(name = "자기 소개", example = "안녕하세요? 백엔드 개발자입니다.")
    @NotBlank
    private String bio;

    @Builder
    public UpdateAvatarRequest(String nickname, String studentId, Major major, String bio) {
        this.nickname = nickname;
        this.studentId = studentId;
        this.major = major;
        this.bio = bio;
    }
}
