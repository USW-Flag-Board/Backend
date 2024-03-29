package com.Flaground.backend.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"message", "payload"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse<T> {
    private final static String SUCCESS_MESSAGE = "성공적으로 요청을 불러왔습니다.";
    private T payload;
    private String message;

    public ApplicationResponse(T payload) {
        this.payload = payload;
        this.message = SUCCESS_MESSAGE;
    }

    public ApplicationResponse() {
        this.message = SUCCESS_MESSAGE;
    }

    public ApplicationResponse(String message) {
        this.message = message;
    }
}
