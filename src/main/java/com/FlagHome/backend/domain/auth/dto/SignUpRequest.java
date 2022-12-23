package com.FlagHome.backend.domain.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @ApiModelProperty(value = "이메일", notes = "회원가입 정보에 적은 이메일")
    private String email;

    @ApiModelProperty(value = "인증 번호", notes = "회원가입 정보 입력 후 USW 메일로 전송되는 6자리 인증번호")
    private String certification;
}
