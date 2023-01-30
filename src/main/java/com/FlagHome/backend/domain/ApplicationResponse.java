package com.FlagHome.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse<T> {
    @Schema(name = "페이로드", description = "응답 데이터")
    private T payload;

    @Schema(name = "API 결과 상태")
    private HttpStatus status;

    @Schema(name = "API 결과 메세지")
    private String message;

    public static <T> ApplicationResponse<Object> of(T payload, HttpStatus status, String message) {
        return ApplicationResponse.builder()
                .payload(payload)
                .status(status)
                .message(message)
                .build();
    }
}
