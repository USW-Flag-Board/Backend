package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.domain.member.entity.enums.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAvatarRequest {
    @Schema(name = "닉네임")
    @NotBlank
    private String nickName;

    @Schema(name = "학번")
    @NotBlank
    private String studentId;

    @Schema(name = "전공")
    @NotNull
    private Major major;

    @Schema(name = "자기 소개", example = "안녕하세요? 백엔드 개발자입니다.")
    @NotBlank
    private String bio;

    @Builder
    public UpdateAvatarRequest(String nickName, String studentId, Major major, String bio) {
        this.nickName = nickName;
        this.studentId = studentId;
        this.major = major;
        this.bio = bio;
    }
}
