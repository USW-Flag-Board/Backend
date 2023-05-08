package com.Flaground.backend.module.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecoveryResponse {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "제한 시간")
    private LocalDateTime deadLine;

    @Builder
    public RecoveryResponse(String email, LocalDateTime deadLine) {
        this.email = email;
        this.deadLine = deadLine;
    }
}
