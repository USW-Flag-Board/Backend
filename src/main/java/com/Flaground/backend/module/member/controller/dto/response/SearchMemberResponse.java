package com.Flaground.backend.module.member.controller.dto.response;

import com.Flaground.backend.module.member.domain.enums.Major;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchMemberResponse {
    @Schema(name = "로그인 아이디")
    private String loginId;

    @Schema(name = "회원 이름", example = "홍길동")
    private String name;

    @Schema(name = "회원 전공", example = "정보보호")
    private String major;

    @QueryProjection
    public SearchMemberResponse(String loginId, String name, Major major) {
        this.loginId = loginId;
        this.name =  name;
        this.major = String.valueOf(major);
    }
}
