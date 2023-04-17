package com.FlagHome.backend.domain.member.controller.dto.response;

import com.FlagHome.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindResultResponse {
    @Schema(name = "로그인 아이디")
    private String loginId;

    @Schema(name = "이메일")
    private String email;

    @Builder
    public FindResultResponse(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }

    public static FindResultResponse from(Member member) {
        return FindResultResponse.builder()
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .build();
    }
}
