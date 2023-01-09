package com.FlagHome.backend.domain.auth.dto;

import com.FlagHome.backend.domain.auth.entity.AuthMember;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {
    @Schema(description = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    public static JoinResponse from(AuthMember authMember) {
        return new JoinResponse(authMember.getEmail());
    }
}
