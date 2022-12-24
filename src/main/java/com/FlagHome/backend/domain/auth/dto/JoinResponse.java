package com.FlagHome.backend.domain.auth.dto;

import com.FlagHome.backend.domain.auth.entity.AuthMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {
    @ApiModelProperty(value = "학교 이메일",
            notes = "재학생 인증 단계에서 화면에 출력할 Response",
            example = "gmlwh124@suwon.ac.kr")
    private String email;

    public static JoinResponse from(AuthMember authMember) {
        return new JoinResponse(authMember.getEmail());
    }
}
