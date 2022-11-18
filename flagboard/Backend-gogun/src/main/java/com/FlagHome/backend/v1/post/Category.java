package com.FlagHome.backend.v1.post;

import lombok.Getter;

@Getter
public enum Category {
    /**
     * 게시글 카테고리
     * 공지사항, 정보게시판, 자유게시판(이모저모??), 스터디 모집
     * notice : 나중에 확장성을 위해 추가적인 작업 필요(ex. dynamic enum)
     */
    notice, information, free, study
}
