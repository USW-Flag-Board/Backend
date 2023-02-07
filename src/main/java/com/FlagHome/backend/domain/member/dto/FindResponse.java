package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.token.entity.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindResponse {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "제한 시간")
    private LocalDateTime deadLine;

    public static FindResponse from(Token findRequestToken) {
        return FindResponse.builder()
                .email(findRequestToken.getKey())
                .deadLine(findRequestToken.getExpiredAt())
                .build();
    }
}
