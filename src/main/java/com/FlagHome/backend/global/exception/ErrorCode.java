package com.FlagHome.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /** 400 - BAD_REQUEST */
    USER_ID_EXISTS(BAD_REQUEST, "아이디가 이미 존재합니다."),
    USER_NOT_EXISTS(BAD_REQUEST, "사용자가 존재하지 않습니다."),
    USER_JOIN_FAILED(BAD_REQUEST, "회원 가입에 실패 했습니다."),
    EMAIL_EXISTS(BAD_REQUEST, "이메일이 이미 존재합니다."),
    PASSWORD_ERROR(BAD_REQUEST, "비밀번호를 확인해주세요."),
    JWT_MALFORMED_EXCEPTION(BAD_REQUEST, "토큰이 올바르지 않습니다."),
    JWT_UNSUPPORTED_EXCEPTION(BAD_REQUEST, "지원하지 않는 토큰 형식입니다."),
    JWT_IllegalARGUMENT_EXCEPTION(BAD_REQUEST, "허용하지 않는 토큰입니다."),
    POST_NOT_EXISTS(BAD_REQUEST, "존재하지 않는 게시물 입니다."),

    /** 401 - UNAUTHORIZED  */
    UNAUTHENTICATED(UNAUTHORIZED, "로그인이 필요한 기능입니다."),
    LOGIN_FAILED(UNAUTHORIZED, "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요."),
    JWT_EXPIRED_EXCEPTION(UNAUTHORIZED, "토큰이 만료되었습니다."),

    /** 403 - FORBIDDEN */
    INVALID_RESTRICTED(FORBIDDEN, "접근 권한이 없습니다."),

    /** 404 - NOT_FOUND */
    INVALID_FOUND(NOT_FOUND, "요청을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
