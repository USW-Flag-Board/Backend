package com.Flaground.backend.global.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Data
@JsonPropertyOrder({"status", "message", "payload"})
public class ApplicationResponse<T> {
    private final static String SUCCESS_MESSAGE = "성공적으로 요청을 불러왔습니다.";

    private T payload;
    private HttpStatus status;
    private String message;

    public ApplicationResponse(T payload) {
        this.payload = payload;
        this.status = OK;
        this.message = SUCCESS_MESSAGE;
    }

    public ApplicationResponse() {
        this.status = OK;
        this.message = SUCCESS_MESSAGE;
    }

    @Builder
    public ApplicationResponse(T payload, HttpStatus status, String message) {
        this.payload = payload;
        this.status = status;
        this.message = message;
    }

    public static <T> ApplicationResponse<Object> of(T payload, HttpStatus status, String message) {
        return ApplicationResponse.builder()
                .payload(payload)
                .status(status)
                .message(message)
                .build();
    }
}
