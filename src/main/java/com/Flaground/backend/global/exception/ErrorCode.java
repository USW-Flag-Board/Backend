package com.Flaground.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /** 400 - BAD_REQUEST */
    REQUEST_VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "잘못된 요청값입니다."),
    LOCKED_ACCOUNT(HttpStatus.BAD_REQUEST, "잠겨있는 계정입니다. 비밀번호를 재설정하기 바랍니다."),
    BANNED_ACCOUNT(HttpStatus.BAD_REQUEST, "일시정지된 계정입니다."),
    WITHDRAW_ACCOUNT(HttpStatus.BAD_REQUEST, "삭제된 계정입니다."),
    BLACKED_EMAIL(HttpStatus.BAD_REQUEST, "가입 제한된 이메일입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰입니다."),
    EXPIRED_AUTHENTICATION_TIME(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다."),
    INACCESSIBLE_POST(HttpStatus.BAD_REQUEST, "삭제된 게시글입니다."),
    VALIDATE_NOT_PROCEED(HttpStatus.BAD_REQUEST, "중복검사를 진행하지 않았습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_RECRUITMENT_ACTIVITY(HttpStatus.BAD_REQUEST, "모집 중인 활동이 아닙니다."),
    NOT_ON_ACTIVITY(HttpStatus.BAD_REQUEST, "진행 중인 활동이 아닙니다."),
    NOT_AUTHOR(HttpStatus.BAD_REQUEST, "작성자가 아닙니다."),
    INVALID_MONTH_RANGE(HttpStatus.BAD_REQUEST, "입력범위에 벗어났습니다."),
    NEVER_LIKED(HttpStatus.BAD_REQUEST, "좋아요를 하지 않았습니다."),
    NOT_CORRECT_BOARD(HttpStatus.BAD_REQUEST, "올바르지 않는 게시판입니다."),

    /** 401 - UNAUTHORIZED  */
    NONE_AUTHORIZATION_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    NOT_ACTIVITY_LEADER(HttpStatus.UNAUTHORIZED, "활동장이 아닙니다."),

    /** 404 - NOT_FOUND */
    URI_NOT_FOUND(HttpStatus.NOT_FOUND, "지원하지 않는 URI입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    AUTH_INFORMATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가입정보입니다."),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하는 신청 내역이 없습니다."),
    ACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 활동입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고이력이 존재하지 않습니다."),
    FIND_REQUEST_NONE(HttpStatus.NOT_FOUND, "아이디/비밀번호 찾기 요청이 존재하지 않습니다."),
    SLEEP_NOT_FOUND(HttpStatus.NOT_FOUND, "휴면계정이 존재하지 않습니다"),

    /**
     * 409 - REQUEST_CONFLICT
     * 저장된 데이터와 일치하는 경우 (technically 따지면 기존 자원과 충돌하는 경우)
     */
    PASSWORD_IS_SAME(HttpStatus.CONFLICT, "기존과 같은 비밀번호는 사용할 수 없습니다."),
    CERTIFICATION_NOT_MATCH(HttpStatus.CONFLICT, "인증번호가 일치하지 않습니다."),
    ALREADY_REPORTED(HttpStatus.CONFLICT,"이미 신고한 대상입니다."),
    ALREADY_APPLIED(HttpStatus.CONFLICT,"이미 신청한 활동입니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT,"이미 좋아요를 눌렀습니다."),
    EMAIL_ID_NOT_MATCH(HttpStatus.CONFLICT, "이메일과 아이디가 일치하지 않습니다."),
    EMAIL_NAME_NOT_MATCH(HttpStatus.CONFLICT, "이메일과 이름이 일치하지 않습니다."),
    TOKEN_NOT_MATCH(HttpStatus.CONFLICT, "토큰의 정보가 일치하지 않습니다."),
    ALREADY_EXIST_BOARD(HttpStatus.CONFLICT, "이미 같은 게시판이 존재합니다."),

    /**
     * 500 - INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의주세요."),
    FILE_CONVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
