package com.FlagHome.backend.domain.board.enums;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;

public enum SearchType {
    TITLE("title", "제목"),
    CONTENT("content", "내용"),
    TITLE_AND_CONTENT("title-and-content", "제목+내용"),
    WRITER("writer", "작성자");

    private final String searchCode;
    private final String explanation;

    SearchType(String searchCode, String explanation) {
        this.searchCode = searchCode;
        this.explanation = explanation;
    }

    public static SearchType of(String searchCode) {
        for(SearchType searchType : SearchType.values()) {
            if(searchCode.equals(searchType.searchCode))
                return searchType;
        }

        throw new CustomException(ErrorCode.SEARCH_CODE_NOT_EXISTS);
    }
}
