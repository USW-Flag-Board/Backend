package com.Flaground.backend.module.post.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.Flaground.backend.module.post.domain.QPost.post;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum SearchPeriod {
    all("전체기간", null),
    one_day("1일", post.createdAt.before(LocalDateTime.now().minusDays(1))),
    one_week("1주일", post.createdAt.before(LocalDateTime.now().minusWeeks(1))),
    one_month("1개월", post.createdAt.before(LocalDateTime.now().minusMonths(1))),
    half_month("6개월", post.createdAt.before(LocalDateTime.now().minusMonths(6))),
    one_year("1년", post.createdAt.before(LocalDateTime.now().minusYears(1)));

    private final String period;
    private final BooleanExpression expression;
}