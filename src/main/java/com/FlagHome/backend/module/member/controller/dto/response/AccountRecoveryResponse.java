package com.FlagHome.backend.module.member.controller.dto.response;

import com.FlagHome.backend.module.token.entity.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRecoveryResponse {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "제한 시간")
    private LocalDateTime deadLine;

    @Builder
    public AccountRecoveryResponse(String email, LocalDateTime deadLine) {
        this.email = email;
        this.deadLine = deadLine;
    }

    public static AccountRecoveryResponse from(Token findRequestToken) {
        return AccountRecoveryResponse.builder()
                .email(findRequestToken.getKey())
                .deadLine(findRequestToken.getExpiredAt())
                .build();
    }
}
