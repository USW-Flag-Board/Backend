package com.FlagHome.backend.v1.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequest {
    @ApiModelProperty(example = "gmlwh124")
    private String loginID;

    @ApiModelProperty(example = "1234")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginID, password);
    }
}
