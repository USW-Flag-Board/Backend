package com.Flaground.backend.module.post.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.Flaground.backend.module.post.domain.QPost.post;

@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum SearchPeriod {
    ALL(null),
    DAY(post.createdAt.after(LocalDateTime.now().minusDays(1))),
    WEEK(post.createdAt.after(LocalDateTime.now().minusWeeks(1))),
    MONTH(post.createdAt.after(LocalDateTime.now().minusMonths(1))),
    HALF_YEAR(post.createdAt.after(LocalDateTime.now().minusMonths(6))),
    YEAR(post.createdAt.after(LocalDateTime.now().minusYears(1)));

    private final BooleanExpression periodExpression;

    public BooleanExpression toExpression() {
        return this.periodExpression;
    }
}
