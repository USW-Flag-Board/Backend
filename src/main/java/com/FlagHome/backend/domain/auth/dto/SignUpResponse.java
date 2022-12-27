package com.FlagHome.backend.domain.auth.dto;

import com.FlagHome.backend.domain.auth.JoinType;
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
public class SignUpResponse {
    @ApiModelProperty(value = "아이디", notes = "회원가입 정보에 적은 아이디")
    private String loginId;

    @ApiModelProperty(value = "이메일", notes = "회원가입 정보에 적은 이메일")
    private String email;

    @ApiModelProperty(value = "유저 가입 구분")
    private JoinType joinType;

    public static SignUpResponse from(AuthMember authMember) {
        return new SignUpResponse(authMember.getLoginId(), authMember.getEmail(), authMember.getJoinType());
    }
}
