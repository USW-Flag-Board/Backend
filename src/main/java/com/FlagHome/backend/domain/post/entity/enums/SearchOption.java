package com.FlagHome.backend.domain.post.entity.enums;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

import static com.FlagHome.backend.domain.member.entity.QMember.member;
import static com.FlagHome.backend.domain.post.entity.QPost.post;

@Getter
@RequiredArgsConstructor
public enum SearchOption {
    TITLE("제목", post.title::contains),
    CONTENT("내용", post.content::contains),
    REPLY("댓글", null),
    CONTENT_AND_REPLY("내용+댓글", post.content::contains),
    AUTHOR("작성자", member.avatar.nickname::like);

    private final String option;
    private final Function<String, BooleanExpression> expression;

    public BooleanExpression getExpression(String keyword) {
        return expression == null ? null : expression.apply(keyword);
    }

    public boolean isContainsReply() {
        return this == REPLY || this == CONTENT_AND_REPLY;
    }
}
