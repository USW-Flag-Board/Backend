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
    ALL("전체기간", null),
    DAY("1일", post.createdAt.before(LocalDateTime.now().minusDays(1))),
    WEEK("1주일", post.createdAt.before(LocalDateTime.now().minusWeeks(1))),
    MONTH("1개월", post.createdAt.before(LocalDateTime.now().minusMonths(1))),
    HALF_YEAR("6개월", post.createdAt.before(LocalDateTime.now().minusMonths(6))),
    YEAR("1년", post.createdAt.before(LocalDateTime.now().minusYears(1)));

    private final String period;
    private final BooleanExpression expression;
}
