//package com.FlagHome.backend.domain.board.v1.enums;
//
//import com.FlagHome.backend.global.exception.CustomException;
//import com.FlagHome.backend.global.exception.ErrorCode;
//
//import java.util.Arrays;
//
//public enum SearchType {
//    TITLE("title", "제목"),
//    CONTENT("content", "내용"),
//    TITLE_AND_CONTENT("title-and-content", "제목+내용"),
//    USER_NAME("user-name", "작성자 이름"),
//    LOGIN_ID("login-id", "작성자 아이디");
//
//    private final String searchCode;
//    private final String explanation;
//
//    SearchType(String searchCode, String explanation) {
//        this.searchCode = searchCode;
//        this.explanation = explanation;
//    }
//
//    public static SearchType of(String searchCode) {
//        return Arrays.stream(SearchType.values())
//                .filter(value -> value.searchCode.equals(searchCode))
//                .findFirst()
//                .orElseThrow(() -> new CustomException(ErrorCode.SEARCH_CODE_NOT_EXISTS));
//    }
//}
