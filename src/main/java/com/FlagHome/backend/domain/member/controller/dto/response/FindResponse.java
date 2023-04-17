package com.FlagHome.backend.domain.member.controller.dto.response;

import com.FlagHome.backend.domain.member.token.entity.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindResponse {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "제한 시간")
    private LocalDateTime deadLine;

    @Builder
    public FindResponse(String email, LocalDateTime deadLine) {
        this.email = email;
        this.deadLine = deadLine;
    }

    public static FindResponse from(Token findRequestToken) {
        return FindResponse.builder()
                .email(findRequestToken.getKey())
                .deadLine(findRequestToken.getExpiredAt())
                .build();
    }
}
