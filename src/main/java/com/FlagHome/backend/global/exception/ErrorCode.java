package com.FlagHome.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /** 400 - BAD_REQUEST */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    LOGIN_ID_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디 입니다."),
    EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
    USER_JOIN_FAILED(HttpStatus.BAD_REQUEST, "회원 가입에 실패 했습니다."),
    NONE_AUTHORIZATION_TOKEN(HttpStatus.BAD_REQUEST, "권한 정보가 없는 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰입니다."),
    TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "토큰의 정보가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_USW_EMAIL(HttpStatus.BAD_REQUEST, "수원대학교 웹 메일 주소가 아닙니다."),
    PASSWORD_IS_SAME(HttpStatus.BAD_REQUEST, "기존과 같은 비밀번호는 사용할 수 없습니다."),
    CATEGORY_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,
            "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)"),
    CERTIFICATION_NOT_MATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),

    /** 401 - UNAUTHORIZED  */
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 기능입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "권한이 없는 토큰입니다."),

    /** 404 - NOT_FOUND */
    INVALID_FOUND(HttpStatus.NOT_FOUND, "요청을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물 입니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글 입니다."),
    AUTH_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "가입 대상이 존재하지 않습니다."),

    /** 500 - INTERNAL_SERVER_ERROR */
    FILE_CONVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
