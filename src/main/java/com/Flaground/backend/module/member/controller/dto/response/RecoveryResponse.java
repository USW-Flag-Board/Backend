package com.Flaground.backend.module.member.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonPropertyOrder({"email", "deadLine"})
public class RecoveryResponse {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "제한 시간")
    private LocalDateTime deadLine;

    public static RecoveryResponse of(String email, LocalDateTime deadLine) {
        return RecoveryResponse.builder()
                .email(email)
                .deadLine(deadLine)
                .build();
    }
}
