package com.Flaground.backend.module.post.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.Flaground.backend.module.post.domain.QPost.post;
import static com.Flaground.backend.module.post.domain.QReply.reply;

@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum SearchOption {
    TITLE(post.title::contains),
    CONTENT(post.content::contains),
    REPLY(reply.content::contains),
    CONTENT_AND_REPLY(keyword -> post.content.contains(keyword).or(reply.content.contains(keyword))),
    AUTHOR(member.avatar.nickname::like);

    private final Function<String, BooleanExpression> expression;

    public BooleanExpression toExpression(String keyword) {
        return expression.apply(keyword);
    }

    public boolean containsReply() {
        return this == REPLY || this == CONTENT_AND_REPLY;
    }
}
