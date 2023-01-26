package com.FlagHome.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /** 400 - BAD_REQUEST */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NONE_AUTHORIZATION_TOKEN(HttpStatus.BAD_REQUEST, "권한 정보가 없는 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰입니다."),
    TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "토큰의 정보가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 아닙니다."),
    NOT_SUPPORT_ACTIVITY(HttpStatus.BAD_REQUEST, "지원하는 활동이 아닙니다."),
    NOT_SUPPORT_LIKE(HttpStatus.BAD_REQUEST, "지원하는 좋아요 형식이 아닙니다."),
    ALREADY_EXISTS_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),
    NOT_EXISTS_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요 해제를 하였습니다."),

    /** 401 - UNAUTHORIZED  */
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 기능입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "권한이 없는 토큰입니다."),
    NOT_ACTIVITY_LEADER(HttpStatus.UNAUTHORIZED, "활동장이 아닙니다."),

    /** 404 - NOT_FOUND */
    INVALID_FOUND(HttpStatus.NOT_FOUND, "요청을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    AUTH_INFORMATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가입정보입니다."),
    BOARD_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 게시판입니다."),
    SEARCH_CODE_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 검색 타입입니다."),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하는 신청 내역이 없습니다."),
    ACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 활동입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고이력이 존재하지 않습니다."),

    /**
     * 409 - REQUEST_CONFLICT
     * 저장된 데이터와 일치하는 경우 (technically 따지면 기존 자원과 충돌하는 경우)
     */
    PASSWORD_IS_SAME(HttpStatus.CONFLICT, "기존과 같은 비밀번호는 사용할 수 없습니다."),
    LOGIN_ID_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    EMAIL_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    CERTIFICATION_NOT_MATCH(HttpStatus.CONFLICT, "인증번호가 일치하지 않습니다."),
    ALREADY_REPORTED(HttpStatus.CONFLICT,"이미 신고한 대상입니다"),
    ALREADY_APPLIED(HttpStatus.CONFLICT,"이미 신청한 활동입니다"),

    /**
     * 422 - UNPROCESSABLE_ENTITY
     * 타입과 문법은 일치하지만 validation 실패 시
     */
    NOT_USW_EMAIL(HttpStatus.UNPROCESSABLE_ENTITY, "수원대학교 웹 메일 주소가 아닙니다."),
    INVALID_PASSWORD(HttpStatus.UNPROCESSABLE_ENTITY, "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)"),

    /** 500 - INTERNAL_SERVER_ERROR */
    FILE_CONVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
