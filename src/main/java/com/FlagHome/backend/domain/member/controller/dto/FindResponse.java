package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.domain.member.token.entity.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FindResponse {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    @NotBlank
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
