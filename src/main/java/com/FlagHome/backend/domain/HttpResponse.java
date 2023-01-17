package com.FlagHome.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.HashMap;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse<T> {
    @Schema(name = "페이로드", description = "응답 데이터")
    private T payload;

    @Schema(name = "API 결과", description = "API를 호출한 결과")
    private HashMap<HttpStatus, String> result;
}
