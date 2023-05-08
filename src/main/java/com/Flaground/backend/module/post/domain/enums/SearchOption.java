package com.Flaground.backend.module.post.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.Flaground.backend.module.post.domain.QPost.post;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum SearchOption {
    title("제목", post.title::contains),
    content("내용", post.content::contains),
    reply("댓글", null),
    content_and_reply("내용+댓글", post.content::contains),
    author("작성자", member.avatar.nickname::like);

    private final String option;
    private final Function<String, BooleanExpression> expression;

    public BooleanExpression getExpression(String keyword) {
        return expression == null ? null : expression.apply(keyword);
    }

    public boolean isContainsReply() {
        return this == reply || this == content_and_reply;
    }
}
