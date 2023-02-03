package com.FlagHome.backend.domain.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonPropertyOrder({"status", "message", "payload"})
public class ApiResponse<T> {
    private final static HttpStatus OK = HttpStatus.OK;
    private final static String SUCCESS_MESSAGE = "성공적으로 요청을 불러왔습니다.";

    @Schema(name = "페이로드", description = "응답 데이터")
    private T payload;

    @Schema(name = "API 결과 상태")
    private HttpStatus status;

    @Schema(name = "API 결과 메세지")
    private String message;

    public ApiResponse(T payload) {
        this.payload = payload;
        this.status = OK;
        this.message = SUCCESS_MESSAGE;
    }

    public ApiResponse() {
        this.status = OK;
        this.message = SUCCESS_MESSAGE;
    }

    @Builder
    public ApiResponse(T payload, HttpStatus status, String message) {
        this.payload = payload;
        this.status = status;
        this.message = message;
    }

    public static <T> ApiResponse<Object> of(T payload, HttpStatus status, String message) {
        return ApiResponse.builder()
                .payload(payload)
                .status(status)
                .message(message)
                .build();
    }
}
