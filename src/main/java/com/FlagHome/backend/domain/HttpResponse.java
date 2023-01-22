package com.FlagHome.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse<T> {
    @Schema(name = "페이로드", description = "응답 데이터")
    private T payload;

    @Schema(name = "API 결과 상태")
    private HttpStatus status;

    @Schema(name = "API 결과 메세지")
    private String message;

    public static <T> HttpResponse<Object> ok(T payload, HttpStatus status, String message) {
        return HttpResponse.builder()
                .payload(payload)
                .status(status)
                .message(message)
                .build();
    }
}
