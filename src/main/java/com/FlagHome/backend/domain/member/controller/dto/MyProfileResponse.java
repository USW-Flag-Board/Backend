package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.domain.member.entity.enums.Major;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyProfileResponse {
    @Schema(name = "닉네임", example = "john")
    private String nickName;

    @Schema(name = "자기소개", example = "안녕하세요?")
    private String bio;

    @Schema(name = "프로필 이미지")
    private String profileImg;

    @Schema(name = "로그인 아이디")
    private String loginId;

    @Schema(name = "이름", example = "문희조")
    private String name;

    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "전공", example = "컴퓨터SW")
    private Major major;

    @Schema(name = "학번", example = "19017041")
    private String studentId;

    @Builder
    @QueryProjection
    public MyProfileResponse(String nickName, String bio, String profileImg, String loginId,
                             String name, String email, Major major, String studentId) {
        this.nickName = nickName;
        this.bio = bio;
        this.profileImg = profileImg;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.major = major;
        this.studentId = studentId;
    }
}
