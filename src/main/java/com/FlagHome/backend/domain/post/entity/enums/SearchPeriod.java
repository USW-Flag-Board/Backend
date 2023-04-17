package com.FlagHome.backend.domain.post.entity.enums;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.FlagHome.backend.domain.post.entity.QPost.post;

@Getter
@RequiredArgsConstructor
public enum SearchPeriod {
    DEFAULT("전체기간", null),
    ONE_DAY("1일", post.createdAt.before(LocalDateTime.now().minusDays(1))),
    ONE_WEEK("1주일", post.createdAt.before(LocalDateTime.now().minusWeeks(1))),
    ONE_MONTH("1개월", post.createdAt.before(LocalDateTime.now().minusMonths(1))),
    HALF_YEAR("6개월", post.createdAt.before(LocalDateTime.now().minusMonths(6))),
    ONE_YEAR("1년", post.createdAt.before(LocalDateTime.now().minusYears(1)));

    private final String period;
    private final BooleanExpression expression;
}
