package com.FlagHome.backend.v1.auth.dto;

import com.FlagHome.backend.v1.member.entity.Member;
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
    @ApiModelProperty(example = "gmlwh124")
    private String loginId;

    public static SignUpResponse of(Member member) {
        return new SignUpResponse(member.getLoginId());
    }
}
